package `fun`.gladkikh.app.fastpallet8.domain.usecase.recalcdb

import `fun`.gladkikh.app.fastpallet8.repository.creatpallet.CreatePalletRepository


class RecalcDbUseCase(private val createPalletRepositoryUpdate: CreatePalletRepository) {
    fun recalc() {
        createPalletRepositoryUpdate.recalculatePallet()
        createPalletRepositoryUpdate.recalculateProduct()
    }
}