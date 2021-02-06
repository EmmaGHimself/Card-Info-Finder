package com.demo.restapi

class CardInfoFinderCloudConfig private constructor() {
    val baseUrl: String?
        get() = Companion.baseUrl

    companion object {
        @JvmStatic
        var instance: CardInfoFinderCloudConfig? = null
            get() {
                if (field == null) {
                    synchronized(CardInfoFinderCloud::class.java) {
                        if (field == null) {
                            field = CardInfoFinderCloudConfig()
                        }
                    }
                }
                return field
            }
            private set
        private var baseUrl: String? = null
    }

    init {
        if (BuildConfig.DEBUG) {
            Companion.baseUrl = BuildConfig.DEV_API_BASE_URL
        } else {
            Companion.baseUrl = BuildConfig.PRODUCTION_API_BASE_URL
        }
    }
}