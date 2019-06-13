<?php

function ext_check(string $sfile) {
    $sext = pathinfo($sfile, PATHINFO_EXTENSION);
    $sext = strtolower($sext);

    if($sext != "wav" && $ext != "mp3") {
        echo "[!]Sound file must be .wav or .mp3";
    }
}

function image_ext_check(string $iext): bool {
    $iext = strtolower($iext);

    if($iext == "png" || $iext == "jpg") {
        echo "OL!!!!!<br>";
        return true;
    }

    echo "machigattttttttt";
    return false;
}


$dir = '/var/www/html/Evin/upload/';
$spath = $dir . basename($_FILES['sound']['name']);

$tsname = $_FILES['sound']['tmp_name'];
echo $tsname . "\n" . $spath . "<br>";

ext_check($_FILES['sound']['name']);

//sound
if(is_uploaded_file($tsname)) {
    chmod($tsname, 0755);

    if(move_uploaded_file($tsname, $spath)) {
        echo "uploaded to " . $spath . "<br>";
    }
    else
        echo "[!]failed to upload<br>";
}
else
    echo "[!]no file<br>";

//image[]
for($i = 0; $i < count($_FILES['image']['name']); $i++) {
    $iext = pathinfo($_FILES['image']['name'][$i], PATHINFO_EXTENSION);
    $ftmpname = $_FILES['image']['tmp_name'][$i];
    $fname = $_FILES['image']['name'][$i];
    echo $ftmpname . " : ". $fname . "<br>";

    if(
        image_ext_check($iext) &&
        is_uploaded_file($ftmpname)
    ) {
        chmod($ftmpname, 0755);
        if(
            move_uploaded_file(
                $ftmpname,
                "./upload/" . $fname
            )
        ) {
            echo $fname . " was uploaded!<br>";
        }
        else {
            echo "[!]failed to upload";
        }
    }
    else {
        echo "[!]No File<br>";
    }
}
?>
