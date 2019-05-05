<?php
$dir = '/var/www/html/Evin/upload/';
$spath = $dir . basename($_FILES['sound']['name']);
$mpath = $dir . basename($_FILES['movie']['name']);

$tsname = $_FILES['sound']['tmp_name'];
$tmname = $_FILES['movie']['tmp_name'];
echo $tsname . "\n" . $spath . "<br>";
echo $tmname . "\n" . $mpath . "<br>";

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
