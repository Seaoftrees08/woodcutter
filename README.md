# Woodcutter
木こりプラグインです. Cutall的な

## 使い方
以下の条件がそろうとき、木こりが発動します
- ゲームモードが Adventure もしくは Survivalである
- 斧で原木を壊した
- 原木の真下のブロックが「土、草、荒い土、ポドゾル、菌糸、深紅のナイリウム、歪んだナイリウム、ネザーラック、泥」のどれかである
- 原木の近くに該当する葉っぱが一つ以上ある
- キノコの場合はキノコブロックが周囲27ブロックに隣接している

ただし、以下の条件を満たす場合は例外的に発動しません.
- 破壊する原木は200を超える場合
  - マングローブは、根と原木を合わせて200
- 破壊する葉が400を超える場合
- キノコブロックが200を超える場合

その他、マングローブの根(最大100まで)もシャベルを使用することで一括破壊できます 

`/woodcutter` もしくは `/wct` コマンドで有効と無効を切りかえることができます

## 最新Plugin要件
Spigot 1.20.x, Java17

## ReleaseNote
- 1.4.1
  - マングローブの原木直下がマングローブの根であっても切れるように
  - 木が大きすぎる場合の警告文を表示
- 1.4.0
  - 桜の木に対応
- 1.3.1
  - シャベルでマングローブの根も破壊できるように
- 1.3.0
  - マングローブに対応
- 1.2.2
  - 巨大なキノコを切れるように
  - 原木200, 葉400のどちらかを超える巨大な木を切れないように
- 1.1.0
  - 斧が壊れた時、鯖にいる全員に音が鳴る問題を修正
  - MinecraftとJavaのバージョンを更新
  - ネザーに生える2種類の木に対応
- 1.0.1
  - 斧の耐久が0の場合無限に使用可能な仕様を修正
  - MIT Licenseで公開
- 1.0.0
  - 自鯖用に作成
