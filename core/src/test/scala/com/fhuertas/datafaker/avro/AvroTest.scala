package com.fhuertas.datafaker.avro

import com.sksamuel.avro4s.{AvroNamespace, AvroSchema, SchemaFor}
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.{AvroRuntimeException, Schema}
import org.scalacheck.Gen
import org.scalatest.{Matchers, WordSpec}

class AvroTest extends WordSpec with Matchers {

  import AvroTest._

  "avro" should {
    "serialize and deserialize case class in avro format with schema" in {
      val element = genSampleAvro.sample.get

      val result = serializeClass(element, withSchema = true)
      val deserialize = deserializeClass(result, withSchema = true)

      new String(result) should include("avro.schema")
      new String(result) should include(element.k1)
      deserialize shouldBe Seq(element)
    }

    "serialize case class without schema in avro" in {
      val element = genSampleAvro.sample.get

      val result = serializeClass(element, withSchema = false)
      val deserialize = deserializeClass(result, withSchema = false)

      new String(result) shouldNot include("avro.schema")
      new String(result) should include(element.k1)
      deserialize shouldBe Seq(element)
    }

    "serialize and deserialize sequence with schema in avro" in {
      val elements = Gen.nonEmptyListOf(genSampleAvro).sample.get

      val result = serializeSeqClass(elements, withSchema = true)
      val deserialize = deserializeClass(result, withSchema = true)

      new String(result) should include("avro.schema")
      elements.map(e => new String(result) should include(e.k1))
      deserialize shouldBe elements
    }

    "not deserialize if there is no schema" in {
      an[AvroRuntimeException] shouldBe
        thrownBy(deserializeClass(serializeClass(genSampleAvro.sample.get, withSchema = true), withSchema = false))
    }

    "not deserialize if there is schema and the element too" in {
      deserializeClass(serializeClass(genSampleAvro.sample.get, withSchema = true), withSchema = true)
    }


    "serialize and deserialize sequence without schema in avro" in {
      val elements = Gen.nonEmptyListOf(genSampleAvro).sample.get

      val result = serializeSeqClass(elements, withSchema = false)
      val deserialize = deserializeClass(result, withSchema = false)

      new String(result) shouldNot include("avro.schema")
      elements.map(e => new String(result) should include(e.k1))
      deserialize shouldBe elements
    }

    "serialize and deserialize generic data with schema" in {
      val elements = Gen.nonEmptyListOf(genSampleAvro).sample.get
      val records = elements.map(e => {
        val record = new GenericData.Record(sampleSchema)
        record.put("k1", e.k1)
        e.k2.foreach(record.put("k2", _))
        record.put("k3", e.k3)
        record
      })

      val result = serializeGenericData(records, withSchema = true)
      val deserialize = deserializeGenericData(result)

      new String(result) should include("avro.schema")
      elements.map(e => new String(result) should include(e.k1))
      deserialize shouldBe records
    }

    "fail if the sequence is empty or with different schema" in {
      val illegalRecords = Seq(new GenericData.Record(sampleSchema), new GenericData.Record(sampleSchema2))
      an[IllegalArgumentException] shouldBe thrownBy(serializeGenericData(Seq.empty[GenericRecord], withSchema = true))
      an[IllegalArgumentException] shouldBe thrownBy(serializeGenericData(illegalRecords, withSchema = false))

    }

    "serialize and deserialize generic data without schema" in {
      val elements = Gen.nonEmptyListOf(genSampleAvro).sample.get
      val records = elements.map(e => {
        val record = new GenericData.Record(sampleSchema)
        record.put("k1", e.k1)
        e.k2.foreach(record.put("k2", _))
        record.put("k3", e.k3)
        record
      })

      val result = serializeGenericData(records, withSchema = false)
      val deserialize = deserializeGenericData(result, Some(sampleSchema))

      new String(result) shouldNot include("avro.schema")
      elements.map(e => new String(result) should include(e.k1))
      deserialize shouldBe records
    }

    "not deserialize the elements without other schema" in {
      val record = new GenericData.Record(sampleSchema2)
      record.put("k1", Gen.choose(1,1000).sample.get.toString)
      val serialized = serializeGenericData(Seq(record), withSchema = false)

      deserializeGenericData(serialized, Some(sampleSchema)) shouldBe empty
    }
  }
}

object AvroTest {

  @AvroNamespace("com.fhuertas")
  case class sample_avro(k1: String, k2: Option[Long] = None, k3: Long)

  case class SampleSchema2(k1: String)

  lazy val genSampleAvro: Gen[sample_avro] = for {
    k1 <- Gen.choose(1, 100000)
    k2 <- Gen.option(Gen.choose(1L, 100000L))
    k3 <- Gen.choose(1, 100000)

  } yield sample_avro(k1.toString, k2, k3)

  implicit val schemaForSample: SchemaFor[sample_avro] = SchemaFor[sample_avro]

  val sampleSchema: Schema = AvroSchema[sample_avro]
  val sampleSchema2: Schema = AvroSchema[SampleSchema2]
}
