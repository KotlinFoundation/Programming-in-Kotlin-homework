package jetbrains.kotlin.course.alias.card

import alias.JsCard
import jetbrains.kotlin.course.alias.util.toJsCard
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cards/")
class CardResource(val service: CardService) {
    @CrossOrigin
    @GetMapping("/card")
    fun getCardByIndex(@RequestParam index: Int): JsCard = service.getCardByIndex(index).toJsCard()

    @CrossOrigin
    @GetMapping("/amount")
    fun getCardsAmount(): Int = CardService.cardsAmount
}
