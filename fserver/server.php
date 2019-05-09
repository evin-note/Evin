<?php

function ext_check(string $sfile, string $mfile) {
    $sext = pathinfo($sfile, PATHINFO_EXTENSION);
    $sext = strtolower($sext);

    if($sext != "wav") {
        exit("[!]Sound file must be .wav");
    }

    $mext = pathinfo($mfile, PATHINFO_EXTENSION);
    $mext = strtolower($mext);

    if($mext != "jpg" && $mext != "png") {
        exit("[!]Movie file must be .jpg or .png");
    }
}

$dir = '/var/www/html/Evin/upload/';
$spath = $dir . basename($_FILES['sound']['name']);
$mpath = $dir . basename($_FILES['movie']['name']);

$tsname = $_FILES['sound']['tmp_name'];
$tmname = $_FILES['movie']['tmp_name'];
echo $tsname . "\n" . $spath . "<br>";
echo $tmname . "\n" . $mpath . "<br>";

ext_check($_FILES['sound']['name'], $_FILES['movie']['name']);

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

//movie
if(is_uploaded_file($tmname)) {
    chmod($tmname, 0755);
    if(move_uploaded_file($tmname, $mpath)) {
        echo "uploaded to " . $mpath . "<br>";
    }
    else
        echo "[!]failed to upload<br>";
}
else
    echo "[!]no file<br>";
?>
