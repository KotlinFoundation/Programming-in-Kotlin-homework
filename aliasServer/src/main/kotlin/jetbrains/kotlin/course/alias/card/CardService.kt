package jetbrains.kotlin.course.alias.card

import org.springframework.stereotype.Service

@Service
class CardService {
    private fun generateCards(): List<Card> = TODO("Not implemented yet")

    private fun List<String>.toWords(): List<Word> = TODO("Not implemented yet")

    fun getCardByIndex(index: Int): Card = TODO("Not implemented yet")
}
