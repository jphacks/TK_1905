# RecMe

[![RecMe](https://github.com/jphacks/TK_1905/blob/feature/yusk-develop/android/app/src/main/res/drawable/logo_horizontal.png)](https://www.youtube.com/watch?v=G5rULR53uMk)

## 製品概要

### Edu Tech

### 背景（製品開発のきっかけ、課題等）

- 英語が全く喋れない
- 日常会話さえできればいいと思っている

#### 背景に対する原因

- 英会話例文集とか見ても、自分が一生使わなさそうなものが多すぎる
- 自分がよく使う文章だけを覚えたい

### 製品説明（具体的な製品の説明）

ユーザーの音を全て録音、言語化し

自分がよく使っている文/ワード順で、その言葉と英訳を表示するアプリ

### 特長

#### 1. 日々の日常会話を全て保存

- タップで集音開始
- アプリを閉じていても、バックグラウンドで集音

#### 2. 自分がよく使う日本語とそれに対応する英文を貯蓄

- 自然言語処理や翻訳機能を利用
- 翻訳結果のスコアも格納

#### 3. 自分専用の例文集を表示

- 発言頻度の高い文章順で表示
- 明らかに翻訳がおかしいとされるものは非表示
- ユーザーが自分で削除も可能

### 解決出来ること

- 自分が確実に使う英会話を優先して、英会話の勉強ができる

### 今後の展望

- 話者分離を用いて、自分の声だけを登録するように
- 「音声認識 -> 文章分離 -> 翻訳」のそれぞれの精度の向上
  - 例えば、日本語を綺麗な日本語に変換してから、翻訳をかけるなど
- edge to edge をちゃんと実装する(android)

## 開発内容・開発技術

### 活用した技術

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

##### Server

- ジョブキューフレームワークを用いて、Google Croud などでの演算は非同期で
  - [user_text.py](https://github.com/jphacks/TK_1905/blob/master/server/main/views/api/user_text.py#L26)
  - [tasks.py](https://github.com/jphacks/TK_1905/blob/master/server/main/tasks.py#L8)
- Google Croud Natural Language API で分けきれない酷い日本語文章を、ヒューリスティックな手法で分解
  - [language.py](https://github.com/jphacks/TK_1905/blob/master/server/main/utils/googleutils/language.py)
  - [tests.py](https://github.com/jphacks/TK_1905/blob/master/server/main/tests.py)
- Google Croud Translation API と Wikipedia のデータで学習した doc2vec を用い、翻訳結果をスコアリング
  - [calc_all_sentence_score.py](https://github.com/jphacks/TK_1905/blob/master/server/main/management/commands/calc_all_sentence_score.py)

##### Android

- バックグランドでアプリが起動していて，音声認識して文字起こしする技術
- 独自のスライドアニメーション
- サーバに送る文字列が日本語として正常になるように制度の悪いセンテンスは送らないようにする機能
