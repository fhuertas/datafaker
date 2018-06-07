package com.fhuertas.datafaker

import java.io.ByteArrayOutputStream

import com.sksamuel.avro4s._
import org.apache.avro.Schema
import org.apache.avro.file.{DataFileReader, DataFileWriter, SeekableByteArrayInput}
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io._
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}

import scala.annotation.tailrec
import scala.util.{Success, Try}

package object avro {
  def serializeClass[T: SchemaFor : ToRecord](element: T, withSchema: Boolean): Array[Byte] = {
    serializeSeqClass(Seq(element), withSchema)
  }

  def deserializeClass[T: SchemaFor : FromRecord](msg: Array[Byte], withSchema: Boolean): Seq[T] = {
    if (withSchema) {
      AvroInputStream.data[T](msg).iterator.toSeq
    } else {
      AvroInputStream.binary[T](msg).iterator.toSeq
    }
  }

  def serializeSeqClass[T: SchemaFor : ToRecord](elements: Seq[T], withSchema: Boolean): Array[Byte] = {
    val stream = new ByteArrayOutputStream()
    val writer = if (withSchema) {
      AvroOutputStream.data[T](stream)
    } else {
      AvroOutputStream.binary[T](stream)
    }
    elements.foreach(e => writer.write(e))
    writer.close()
    stream.toByteArray
  }

  def serializeGenericData(records: Seq[GenericRecord], withSchema: Boolean): Array[Byte] = {
    require(records.nonEmpty, "Empty list can not be serialized to avro")
    require(records.forall(_.getSchema.equals(records.head.getSchema)), "all records should have the same schema")
    val schema = records.head.getSchema
    val writer = new SpecificDatumWriter[GenericRecord](schema)
    val out = new ByteArrayOutputStream()
    if (withSchema) {
      val dataFileWriter = new DataFileWriter(writer)
      dataFileWriter.create(schema, out)
      records.foreach(dataFileWriter.append)
      dataFileWriter.close()
    } else {
      val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
      records.foreach(writer.write(_, encoder))
      encoder.flush()
    }
    out.close()
    out.toByteArray
  }

  def deserializeGenericData(msg: Array[Byte], oSchema: Option[Schema] = None): Seq[GenericRecord] = {
    oSchema match {
      case Some(schema) =>
        val reader: DatumReader[GenericRecord] = new SpecificDatumReader[GenericRecord](schema)
        val decoder: Decoder = DecoderFactory.get().binaryDecoder(msg, null)
        extractRecWithoutSchema(reader, decoder)
      case None =>
        val reader: DatumReader[GenericRecord] = new SpecificDatumReader[GenericRecord]()
        val fileReader = new DataFileReader(new SeekableByteArrayInput(msg), reader)
        extractRec(fileReader)
    }
  }

  @tailrec
  def extractRecWithoutSchema(reader: DatumReader[GenericRecord],
                              decoder: Decoder,
                              acc: Seq[GenericRecord] = Seq.empty[GenericRecord]): Seq[GenericRecord] = {
    Try(reader.read(null, decoder)) match {
      case Success(record) => extractRecWithoutSchema(reader, decoder, acc :+ record)
      case _ => acc
    }
  }

  @tailrec
  def extractRec(fileReader: DataFileReader[GenericRecord],
                 acc: Seq[GenericRecord] = Seq.empty[GenericRecord]): Seq[GenericRecord] = {
    if (fileReader.hasNext) {
      val newElement = fileReader.next
      extractRec(fileReader, acc :+ newElement)
    } else {
      acc
    }
  }
}
