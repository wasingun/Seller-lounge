# Seller-lounge

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/gradle-8.0-green.svg)](https://gradle.org/)
[![Android Studio](https://img.shields.io/badge/Android%20Studio-2022.3.1%20%28Giraff%29-green)](https://developer.android.com/studio)
[![minSdkVersion](https://img.shields.io/badge/minSdkVersion-24-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![targetSdkVersion](https://img.shields.io/badge/targetSdkVersion-34-orange)](https://developer.android.com/distribute/best-practices/develop/target-sdk)

***

<h1 align="center">
<img src="https://github.com/wasingun/Seller-lounge/assets/121553658/316f73ba-b07f-4f5b-9c26-af622f9b5e43" width="500px"> </h1>

Seller-lounge는 제조사와 소매상을 연결하는 비즈니스 소셜 네트워크 서비스(SNS) 앱으로, 제조사는 Seller-lounge를 통해 소매상을 찾아 자신의 상품 판로를 개척할 수 있으며, 온라인 쇼핑몰 판매자는 새로운 상품을 발견하고 관련 쇼핑 검색 및 트렌드 분석 데이터를 확인할 수 있습니다. 이 앱은 사용자들이 다양한 상품 카테고리에 게시물, 이미지 및 문서를 자유롭게 등록할 수 있도록 게시판 기반 기능을 제공하며, 앱 내에서 필요한 상품을 즉시 검색할 수 있는 쇼핑 검색 기능과 온라인 쇼핑 트렌드를 비교 및 분석할 수 있는 트렌드 비교 기능도 함께 제공합니다.

## **기술 스택**

- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- JetPack
    - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
    - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    - [Room](https://developer.android.com/training/data-storage/room)
    - [DataBinding](https://developer.android.com/topic/libraries/data-binding)
    - [Navigation](https://developer.android.com/guide/navigation)
    - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Hilt](https://dagger.dev/hilt/)
- [Coil](https://coil-kt.github.io/coil/)
- [GSON](https://github.com/google/gson)
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit)
- [Firebase](https://firebase.google.com/)
    - Realtime DB
    - Storage
    - Auth
    - Crashlytics
- Naver Open API
    - [쇼핑인사이트](https://developers.naver.com/docs/serviceapi/datalab/shopping/shopping.md#%EC%87%BC%ED%95%91%EC%9D%B8%EC%82%AC%EC%9D%B4%ED%8A%B8-%EB%B6%84%EC%95%BC%EB%B3%84-%ED%8A%B8%EB%A0%8C%EB%93%9C-%EC%A1%B0%ED%9A%8C)
    - [쇼핑검색](https://developers.naver.com/docs/serviceapi/search/shopping/shopping.md#%EC%87%BC%ED%95%91)
 
- ## **주요기능 소개**

<div align="center">

| 로그인 | 게시판 카테고리 | 게시물 검색 |
| :---------------: | :---------------: | :---------------: |
| <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/b52a3a57-1685-4a8a-acee-be1480229a5c" align="center" width="250px"/> | <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/f3415396-6c99-4e84-93e8-401074658a39" align="center" width="250px"/> | <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/441da6aa-1186-4e4a-a6be-2b61fcffc1d7" align="center" width="250px"/> |

</div>

<div align="center">

| 다운로드 및 알림 | 게시물 상세화면 이미지 | 게시물 업로드 |
| :---------------: | :---------------: | :---------------: |
| <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/40d6f2df-87bc-4035-974a-d20bae96adf0" align="center" width="250px"/> | <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/2b54cf2f-a1c7-42ab-a0ca-b4c0956c9199" align="center" width="250px"/> | <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/81928a59-9724-49da-8eac-c3974951e05a" align="center" width="250px"/> |

</div>

<div align="center">

| 게시물 수정 | 게시물 삭제 | 다크모드 |
| :---------------: | :---------------: | :---------------: |
| <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/58c74d88-0f7b-4c31-841e-db393a7eaf16" align="center" width="250px"/> | <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/45460723-10e1-496e-8a1c-ffd727bbfc13" align="center" width="250px"/> | <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/6c0feb10-6c02-4596-ba6b-ed4d51038810" align="center" width="250px"/> |

</div>

<div align="center">

| 최근 열람 게시물 | 로그아웃 |
| :---------------: | :---------------: |
| <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/131002f4-6613-49ca-b250-7dcf703ddfbc" align="center" width="250px"/> | <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/8ba25fd7-595a-416f-8a08-2fa25ae15ae7" align="center" width="250px"/> |

</div>

<div align="center">

| 상품 검색 | 트랜드 검색 |
| :---------------: | :---------------: |
| <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/ac00562e-35a3-4b7c-86ef-1023d144ae4a" align="center" width="250px"/> | <img src="https://github.com/wasingun/Seller-lounge/assets/121553658/c261fa8f-470a-4a10-9659-078a89fd1db6" align="center" width="250px"/> |

</div>

## Architecture

<p align = 'center'>
<img width = '600' src = "https://github.com/wasingun/Seller-lounge/assets/121553658/3dfbdec2-c762-45a8-80d4-53fec52e82b8">
</p>

## Figma design

<p align = 'center'>
<img src = "https://github.com/wasingun/Seller-lounge/assets/121553658/8ef5a464-633a-4a9a-9f6b-451bd7d91099">
</p>
