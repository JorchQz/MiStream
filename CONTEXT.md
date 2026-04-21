# MiStream — Contexto del Proyecto

## Qué es
App Android TV/Fire TV en Kotlin que unifica:
- IPTV con Xtream Codes (como TiviMate)
- Películas/Series con Torrentio + Real-Debrid (como Stremio)

## Estado actual
- Fase 1 en desarrollo: módulo Películas/Series
- Fase 2 pendiente: módulo IPTV

## Stack técnico
- Kotlin + ViewBinding + Coroutines
- Hilt (inyección de dependencias)
- Retrofit (APIs REST)
- ExoPlayer/Media3 (reproductor)
- Room + DataStore (base de datos local)
- Glide (imágenes)
- TMDB API (metadata de películas/series)
- Torrentio API (fuente de streams)
- Real-Debrid API (resolver links)

## Package base
com.mistream.app

## Estructura
app/src/main/java/com/mistream/app/
├── MiStreamApp.kt
├── di/
│   └── AppModule.kt          ✅ COMPLETO
├── data/
│   ├── model/
│   │   └── Models.kt         ✅ COMPLETO
│   ├── api/
│   │   ├── TmdbApiService.kt
│   │   ├── TorrentioApiService.kt
│   │   └── RealDebridApiService.kt
│   └── repository/
│       ├── PreferencesRepository.kt
│       ├── TmdbRepository.kt
│       ├── StreamRepository.kt
│       └── RealDebridRepository.kt
└── ui/
    ├── MainActivity.kt
    ├── MainFragment.kt
    ├── MainViewModel.kt
    ├── MediaAdapter.kt
    ├── detail/
    │   ├── DetailActivity.kt
    │   ├── DetailViewModel.kt
    │   └── StreamsAdapter.kt
    ├── player/
    │   └── PlayerActivity.kt
    ├── search/
    │   ├── SearchFragment.kt
    │   ├── SearchViewModel.kt
    │   └── SearchResultsAdapter.kt
    └── settings/
        ├── SettingsActivity.kt
        └── SettingsViewModel.kt

## APIs y tokens
- TMDB API Key: 37f0f3da6ff530a870cfd96cce4e55cf
- TMDB Base URL: https://api.themoviedb.org/3/
- Torrentio Base URL: https://torrentio.strem.fun/
- Real-Debrid Base URL: https://api.real-debrid.com/rest/1.0/
- Real-Debrid token del usuario: guardado en DataStore, cargado en RealDebridTokenHolder.token
- IPTV Xtream Codes URL: http://allinonestream.fans:8080

## Prioridad de streams (orden de preferencia)
1. Latino 4K (sortPriority = 24)
2. Latino 1080p (sortPriority = 23)
3. Latino 720p (sortPriority = 22)
4. Castellano 4K (sortPriority = 14)
5. Castellano 1080p (sortPriority = 13)
6. Resto

## Proveedores Torrentio activos
cinecalidad, mejortorrent, yts, eztv, rarbg, 1337x, thepiratebay, torrentgalaxy, magnetdl

## Proveedores Torrentio excluidos
rutor, rutracker, torrent9, comando, bluDV (ruso/portugués/italiano)

## Clases clave ya implementadas
- RealDebridTokenHolder (objeto en AppModule.kt) — token en memoria
- TmdbTokenHolder (objeto en AppModule.kt) — API key en memoria
- StreamItem.sortPriority — lógica de priorización