<?php
include "/var/www/html/evin/API/src/pdf.php";

header('Content-type: application/json; charset=utf-8');

if(count($argv) != 2) {
    exit("error!");
}

$note = new EvinNote($argv[1], false);

/*
 *  NOTE HEADER
 */
$note->write("<h1>----- NOTE -----</h1>");
$note->write($note->get_timestamp());

/*
 *  Contents
 */
$note->generate_core();

/*
 *  NOTE END
 */
$note->send("/var/www/html/evin/API/dumped-note/marimo.pdf");
?>
