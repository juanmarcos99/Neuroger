# NeuroGer MCI (Android)

Aplicación Android para evaluación y seguimiento neurocognitivo (MCI). Incluye gestión de sujetos, cuestionarios, evaluaciones y exportación de resultados.

## Características
- Actividad de **login/licencia** (arranque principal).
- Pantallas de **splash**, **home**, **acerca de** y módulos de evaluación.
- Soporte para **captura/edición** de datos (sujetos, cuidadores, antecedentes patológicos, etc.).
- **Exportación** de datos (servicios de borrado/exportación y generación de Excel/JSON).
- Librería de **base de datos** local (módulo `neuroger.database`).
- Librería de **tema/controles** (módulo `neuroger.theme`).
- Módulo nativo con **C/C++ (NDK/CMake)** (carpeta `app/src/main/cpp`).

## Tecnologías
- Android Gradle Plugin (AGP): `com.android.tools.build:gradle:4.2.2`
- SDK (Android):
  - `compileSdkVersion`: 28
  - `targetSdkVersion`: 28
  - `minSdkVersion`: 21
- Build Tools: `30.0.2` (en `app/build.gradle`)
- Gradle multi-módulo:
  - `libraries/neurodevelopment-neuroger-database/neuroger.database`
  - `libraries/neurodevelopment-neuroger-theme/neuroger.theme`
  - `ViewPagerIndicator`
- Dependencias destacadas:
  - AndroidX (appcompat, recyclerview, constraintlayout, etc.)
  - Gson
  - Picasso
  - Apache POI
  - Kotlin stdlib

## Requisitos (versiones)
- **Android Studio** compatible con **Gradle/AGP 4.2.2**.
- **Android SDK**:
  - **compileSdk / targetSdk**: 28
  - **minSdk**: 21
- **Build Tools**: `30.0.2`
- **CMake** (si compilas el módulo nativo): `3.18.1`
- **NDK**: opcional pero recomendado si vas a compilar la parte nativa (usa `externalNativeBuild` en `app/build.gradle`; verifica que esté instalado en Android Studio).

## Cómo compilar
1. Abrir el proyecto en **Android Studio**.
2. Sincronizar Gradle.
3. Compilar y ejecutar en un emulador/dispositivo.

## Estructura del proyecto
- `app/`: aplicación principal
  - `src/main/AndroidManifest.xml`: actividades, providers y servicios
  - `src/main/cpp/`: código nativo y CMake
- `libraries/`
  - `neurodevelopment-neuroger-database/`: base de datos/ContentProvider
  - `neurodevelopment-neuroger-theme/`: tema y componentes
- `ViewPagerIndicator/`: componente de UI

## Permisos, proveedores y servicios
- Permisos de almacenamiento para compatibilidad:
  - `READ_EXTERNAL_STORAGE` y `WRITE_EXTERNAL_STORAGE`
- `FileProvider` (para compartir archivos): `...${applicationId}.fileprovider`
- Provider de base de datos:
  - `NeurogerContentProvider` con autoridad `software.cneuro.neuroger.provider`
- Servicios de exportación:
  - `JsonExportIntentService`
  - `ExcelExportIntentService`

## Construcción (release)
En `release`, el APK se renombra a **`NeuroGerPesquisa.apk`**.

