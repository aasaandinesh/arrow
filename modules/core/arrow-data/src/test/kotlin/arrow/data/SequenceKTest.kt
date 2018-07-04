package arrow.data

import arrow.Kind
import arrow.instances.eq
import arrow.instances.extensions
import arrow.test.UnitSpec
import arrow.test.laws.*
import arrow.typeclasses.Eq
import arrow.typeclasses.Show
import io.kotlintest.properties.Gen
import io.kotlintest.runner.junit4.KotlinTestRunner
import org.junit.runner.RunWith

@RunWith(KotlinTestRunner::class)
class SequenceKTest : UnitSpec() {

  init {

    val eq: Eq<Kind<ForSequenceK, Int>> = object : Eq<Kind<ForSequenceK, Int>> {
      override fun Kind<ForSequenceK, Int>.eqv(b: Kind<ForSequenceK, Int>): Boolean =
        toList() == b.toList()
    }

    val show: Show<Kind<ForSequenceK, Int>> = object : Show<Kind<ForSequenceK, Int>> {
      override fun Kind<ForSequenceK, Int>.show(): String =
        toList().toString()
    }

    ForSequenceK extensions {
      testLaws(
        EqLaws.laws(SequenceK.eq(Int.eq())) { sequenceOf(it).k() },
        ShowLaws.laws(show, eq) { sequenceOf(it).k() },
        MonadLaws.laws(this, eq),
        MonoidKLaws.laws(this, this, eq),
        MonoidLaws.laws(SequenceK.monoid(), Gen.int().random().k(), eq),
        TraverseLaws.laws(this, this, { n: Int -> SequenceK(sequenceOf(n)) }, eq)
      )
    }
  }
}
