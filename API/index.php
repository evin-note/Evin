<?php
include "./TCPDF/tcpdf.php";

$pdf = new TCPDF();
$pdf->Addpage();

$pdf->SetFont("kozgopromedium", "", 10);

$content = <<< EOF
<h1>PDF Testttttttttt</h1>
<p>
    PDFPDFPDFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
    PDFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
</p>
EOF;

$pdf->writeHTML($content);
$pdf->Output("marimo.pdf", 'I');
?>
