package drodobyte.core.util

data class Env(
    val build: Build,
    val data: Data,
    val servers: List<Server>
) {
    val isRelease = build.isRelease
    val isDebug = build.isDebug
    val isMock = data.isMock
    val isReal = data.isReal
}

enum class Build {
    Release, Debug;

    val isDebug get() = this == Debug
    val isRelease get() = this == Release
}

enum class Data {
    Mock, Real, Staging, Prod;

    val isMock get() = this == Mock
    val isReal get() = this == Real
    val isStaging get() = this == Staging
    val isProd get() = this == Prod
}

data class Server(val scheme: String, val domain: String) {
    override fun toString() = "$scheme://$domain"
}
