# conditionalskin

## 前提

mincraft version: 1.20.1

前提mod: fabric

forgeで利用したい場合はForgified Fabric APIで検索

## Conditional Skin Configuration

この設定では、特定の条件に基づいて異なるスキンを適用することができます。

### 設定手順

1. **利用したいスキンを格納したリソースパックを導入**  
  例では以下のリソースパックを導入
```
root/
├── assets/
│   └── skins/
│       ├── skin1.png
│       └── skin2.png
└── pack.mcmeta
```

2. **設定ファイルの設置**  
   `config` フォルダ内に `conditionalskin.json` という名前のファイルを作成します。

2. **設定内容**  
   `conditionalskin.json` の中に、以下の形式でスキンと条件を設定します。

   ```json
   {
       "skins": {
           "skin_name1": "skins:skin1.png",
           "skin_name2": "skins:skin2.png"
       },
       "skinConditions": {
           "skin_name1": [
               "isTouchingWater"
           ],
           "skin_name_2": [
               "isInLava"
           ]
       }
   }

3. 設定項目の説明

- **`skins`**: ここでは、リソースパック内の任意のスキン画像を指定します。パスは `namespace:ファイル名` の形式で記述します。
   
- **`skinConditions`**: 各スキンに適用する条件を設定します。スキン名をキーに、条件を配列として設定します。条件はANDで結合され、すべて満たされた場合にスキンが適用されます。

4. 利用可能な条件

設定で使用できる条件の一覧は、`availableConditionUnit.json` を参照してください。
