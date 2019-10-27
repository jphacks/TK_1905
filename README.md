# RecMe

[![RecMe](https://github.com/jphacks/TK_1905/blob/master/images/logo_with_subtitle.png)](https://youtu.be/7AiTSl1K27c)

↑動画はロゴをクリック

## 製品概要

### Education x  Tech

### コンセプト

> **自分で作る 自分のための 英語学習アプリ**

### 背景と課題

> **普段自分が話す表現を、ただ英語で話したいだけ**

- 咄嗟に英語が出てこない
- 日常会話さえできればいいと思っている
- 故に，猛勉強する気は無い

#### 原因

- 英会話例文集とかを見ても使わなさそうな場面の英会話が多すぎて、実際の会話で役立てる場面がごくわずかである
  - 勉強する労力に対して実際に英会話の場面で使える数が見合っていない
  - もっと他に覚えるべき文章があるはず

### 製品説明（具体的な製品の説明）

ユーザの発言をアプリで録音・言語化し、**自分専用の学習データ** を作る。
**自分がよく使っている文章順で** その文と英訳を表示し英会話の勉強に役立てるアプリ

### 特長

#### 1. 日々の日常会話を全て保存

<img src="https://github.com/jphacks/TK_1905/blob/master/images/recording2.png" width="320px">

- タップで集音開始
- アプリを閉じていても、バックグラウンドで集音
- 認識精度の悪いセンテンスは、端末側で送らないようにする機能
- 文章をサーバーに送信

#### 2. 自分がよく使う日本語とそれに対応する英文を貯蓄

- 自然言語処理や翻訳機能を利用
- 翻訳結果のスコアを doc2vec などで計算し格納

#### 3. 自分専用の例文集を表示

<img src="https://github.com/jphacks/TK_1905/blob/master/images/script.png" width="320px">

- 発言頻度の高い文章順で表示
- 明らかに翻訳がおかしいとされるものは非表示
- ユーザーが自分で削除も可能
- 文章をタップすることで、音声読み上げが可能

#### 4. 自分専用の例文集を問題形式で表示

<img src="https://github.com/jphacks/TK_1905/blob/master/images/practice1.png" width="320px">

- 英語を隠して表示
- タップで日本語も出せる
- スワイプで次の問題へ

### 解決出来ること

**英会話の学習にそこまで時間かけれない人** でも、 **自分が確実に使う英会話を優先する** ことにより、 **効率的に英会話の勉強** ができる

### 今後の展望

- 話者分離を用いて、自分の声だけを登録するように
- 「音声認識 -> 文章分離 -> 翻訳」のそれぞれの精度の向上
  - 例えば、日本語を綺麗な日本語に変換してから、翻訳をかけるなど
- edge to edge をちゃんと実装する(android)
- score での文章の表示非表示の閾値をユーザーがイケてるUIから設定できるように

## 開発内容・開発技術

### 活用した技術

#### アーキテクチャ図

<img src="https://github.com/jphacks/TK_1905/blob/master/images/architecture.png">

#### API・データ

* Google Croud Natural Language API
* Google Croud Translation API

#### フレームワーク・ライブラリ・モジュール

* バックエンド
  * インフラ: nginx, uwsgi, sakura vps
  * サーバーサイド: python, django
  * データベース: sqlite3, redis
  * ジョブキュー: celery
* アプリケーション
  * android: kotlin, kotlin coroutine, dagger, mvvm, android speech recognizer
* デザイン
  * adobe illustrator, adobe photoshop, adobe XD, adobe affter effects 

#### デバイス

* android 端末
  * e.g. pixel 3a, pixel 4

### 研究内容・事前開発プロダクト（任意）

なし

### 独自開発技術（Hack Dayで開発したもの）

すべて二日間で開発

#### 2日間に開発した独自の機能・技術

##### Android

- バックグランドでアプリが起動していて，音声認識して文字起こしする技術
    - [SpeechRecognizeService.kt](https://github.com/jphacks/TK_1905/blob/master/android/app/src/main/java/jp/co/myowndict/speechrecognize/SpeechRecognizeService.kt)
- 独自のスライドアニメーション
    - [MainFragment.kt](https://github.com/jphacks/TK_1905/blob/8a8d3951c83d789ce4e61f0712502312994706b3/android/app/src/main/java/jp/co/myowndict/view/main/MainFragment.kt) の46〜54行目
    - [DictFragment](https://github.com/jphacks/TK_1905/blob/master/android/app/src/main/java/jp/co/myowndict/view/main/DictFragment.kt) と[RecordingFragment](https://github.com/jphacks/TK_1905/blob/master/android/app/src/main/java/jp/co/myowndict/view/main/RecordingFragment.kt)のstartTagAnimation()関数
- サーバに送る文字列が日本語として正常になるように精度の悪いセンテンスは送らないようにする機能
    - [SpeechRecognizeService.kt](https://github.com/jphacks/TK_1905/blob/master/android/app/src/main/java/jp/co/myowndict/speechrecognize/SpeechRecognizeService.kt#L215) の215行目

##### Server

- ジョブキューフレームワークを用いて、Google Croud などでの演算は非同期で
  - [user_text.py](https://github.com/jphacks/TK_1905/blob/master/server/main/views/api/user_text.py#L26)
  - [tasks.py](https://github.com/jphacks/TK_1905/blob/master/server/main/tasks.py#L8)
- Google Croud Natural Language API で分けきれない酷い日本語文章を、ヒューリスティックな手法で分解
  - [language.py](https://github.com/jphacks/TK_1905/blob/master/server/main/utils/googleutils/language.py)
  - [tests.py](https://github.com/jphacks/TK_1905/blob/master/server/main/tests.py)
- Google Croud Translation API と Wikipedia のデータで学習した doc2vec を用い、翻訳結果をスコアリング
  - [calc_all_sentence_score.py](https://github.com/jphacks/TK_1905/blob/master/server/main/management/commands/calc_all_sentence_score.py)
