package `fun`.gladkikh.app.fastpallet8.domain.model


enum class Status(val id: Int, val fullName: String) {
    NEW(1, "Новый"), READY(2, "Подготовлен"),
    LOADED(3, "Загружен"), UNLOADED(4, "Выгружен");

    companion object {
        fun getStatusById(id: Int?): Status? {
            return when (id) {
                1 -> NEW
                2 -> READY
                3 -> LOADED
                4 -> UNLOADED
                else -> null
            }
        }

        fun getStatusByString(str: String?): Status {
            return when {
                str.equals("Новый", true) -> NEW
                str.equals("Готов к выгрузке", true) -> READY
                str.equals("Выгружен", true) -> LOADED
                str.equals("Загружен", true) -> UNLOADED

                else -> throw Throwable("Неизвестный статус")
            }

        }

    }
}