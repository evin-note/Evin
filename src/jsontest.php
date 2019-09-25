<?php

$encoded1 = base64_encode(file_get_contents('/var/www/html/evin/upload/result.png'));
$encoded2 = base64_encode(file_get_contents('/var/www/html/evin/upload/suki.png'));

$arr = array(
    array(
        'picture' => $encoded1,
        'text'    => "3年生の2学期までしかないですねそんなん気にする人は一つダイヤ407個のやりそうなんですねそれで今回は本当の男ですよろしかっ運動に音が劇的について集中的に皆さん勉強するっていうとそれから皆さんが来た日だったら等速円運動と単振動が今回して入りすぎてたんだ家で勉強しましたよねでも勉強の仕方がないんだけどもうちょっとかかりそう
この一週間でウエストが今回の目的ですですから1年生の方がいいかもしれないけどもしかして考えればとしてまだ慣れてないところもあるかもしれないから府中してもらいたいしもしかしたらこの問題はそれでいきましょうどうですか"
    )
);

$note_json = json_encode($arr);

file_put_contents('data.json', $note_json);

?>

