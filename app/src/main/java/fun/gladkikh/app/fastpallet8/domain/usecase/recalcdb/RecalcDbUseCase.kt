package `fun`.gladkikh.app.fastpallet8.domain.usecase.recalcdb

import `fun`.gladkikh.app.fastpallet8.repository.CreatePalletRepository


class RecalcDbUseCase(private val createPalletRepositoryUpdate: CreatePalletRepository) {
    fun recalc() {
        createPalletRepositoryUpdate.recalcPallet()
        createPalletRepositoryUpdate.recalcProduct()
    }
}