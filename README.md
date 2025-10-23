# 🔍🐾 Catch
> ### PBL II 프로젝트 / 졸업 작품 (2인)
> ### 주제 : LLM과 DeepVoice 모델 기반 보이스피싱 탐지 애플리케이션
> ### 역할 : 통화 시스템 기획 및 설계, 애플리케이션 개발
<img src="https://github.com/user-attachments/assets/2b2c17f6-592b-43ae-a26d-ecd5aba80299" width="700"/>

<br><br> 

## 💻 개발 환경
- 클라이언트 개발 플랫폼: React Native (Android 지원, JavaScript 기반) <br> 
- 개발 언어: Java, Kotlin, JavaScript <br> 
- IDE: IntelliJ IDEA, VS Code  <br> 
- DB : MongoDB <br> 
<br>

## Android 지원 버전
- minSdkVersion: 24
- targetSdkVersion: 35 
- compileSdkVersion: 35
<br><br>

##  주요 기능

### 1️⃣ 시스템 기반 통화 녹음 및 자동 전송
기존 Android 정책에서는 **서드파티 앱의 통화 녹음이 제한**되어 있으나, **기기의 기본 통화 녹음 시스템을 활용하여 파일을 탐지**하고 <br>
녹음 완료 후 서버로 자동 전송되도록 구현함으로써 **제한된 기능을 구현**
<br>

### 2️⃣ LLM & DeepVoice 기반 보이스피싱 탐지
듀얼 파이프라인 구조로 텍스트 + 음성 동시 분석

텍스트 분석: 파인튜닝된 KoBERT 모델을 통해 통화 내용의 문맥적 위험도 분석

음성 분석: MFCC 음성 특징을 추출한 후, CNN-LSTM 기반 모델로 딥보이스(속임수 음성) 여부 판별

두 결과를 종합하여 최종 보이스피싱 위험도 판단
<br>

### 3️⃣ 통화 내용 전처리 및 기록
화자 분리: 최대 2명의 화자로 제한하여, 각 화자의 음성 구간을 분리

Whisper STT: 분리된 화자별 음성을 텍스트로 변환

이후 결과는 통화 내역에 자동 저장되어 사용자에게 제공됨
<br><br> 

## 아키텍처
<img src="https://github.com/user-attachments/assets/3dbfc92b-2087-40f5-aa18-f3bdcd195590" width="700"/>
<br><br> 

## ERD
![image](https://github.com/user-attachments/assets/4235d9b4-788e-4121-a65f-18fc3697bdaa)
<br><br> 

## API 명세서
![image](https://github.com/user-attachments/assets/72ea5517-a2eb-490c-9d0a-c03bca3b7e70)
<br><br> 

## 프로토타입
![image](https://github.com/user-attachments/assets/3ebe798b-0b8c-4934-8e23-3f9be91ccf99)

<br><br>

## 실행 화면
![image](https://github.com/user-attachments/assets/b96c7ab4-3784-4cc7-9c87-3e93a45c0e42)
<br>
![image](https://github.com/user-attachments/assets/ce0a813e-bf92-4904-be2d-3d82ea4b23c9)
<br>
![image](https://github.com/user-attachments/assets/84ee990e-0da9-4fc6-a6ad-a42435ca34cf)
<br>
<img src="https://github.com/user-attachments/assets/a7d4c616-7c5b-465b-badb-e9dcd93b3bd0" width="300"/>
<br><br>
## 시연 영상
https://github.com/user-attachments/assets/ef2ee696-d539-41ec-bf6b-ab88ca1eea21

https://github.com/user-attachments/assets/9556e113-231d-40b1-b85b-6813ae5bfa25

https://github.com/user-attachments/assets/7efe335d-3361-4ebf-a9a5-97c4d19c05a8


