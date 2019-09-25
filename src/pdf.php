<?php

include "./TCPDF/tcpdf.php";

class EvinNote {
    public $content;
    private $pdf;
    private $json;
    private $json_length;
    private $counter;
    private $dumphtml;
    private $cur_ratio;

    public function __construct(string $json_name, bool $d) {
        $this->pdf = new TCPDF();
        $this->pdf->Addpage();
        $this->pdf->SetFont("kozgopromedium", "", 10);

        $jsonfile = get_text_from_file($json_name);

        $this->json = json_decode($jsonfile, true);
        $this->json_length = count($this->json);
        $this->counter = 0;

        $this->dumphtml = $d;
    }

    public function write(string $str) {
        if($this->dumphtml) {
            echo $str; 
        }
        else
            $this->pdf->writeHTML($str);
    }

    private function put_picture() {
        $pic = base64_decode($this->json[$this->counter]['picture']);

        $filepath = 'test' . $this->counter . '.png';

        file_put_contents($filepath, $pic);

        list($width, $height, $type, $attr) = getimagesize($filepath);
        $this->cur_ratio = $height / $width;

        if($this->dumphtml) {
            $name = sprintf("<img src='%s' width='180'>", $filepath);

            echo $name;
            echo "<br>";
        }
        else {
            $x = $this->pdf->getX();
            $y = $this->pdf->getY();

            $this->pdf->Image($filepath, $x, $y, 180.0);
        }
    }

    private function put_text() {
        $text = $this->json[$this->counter]['text'];

        if($this->dumphtml) {
            echo $text . "<br>";
        }
        else {
            $this->write($text);
        }
    }

    public function generate_core() {
        for(; $this->counter < $this->json_length; $this->counter++) {
            $this->put_picture();
            $this->skip_image();
            $this->put_text();
        }
    }

    public function jsondump() {
        echo $this->json . "\n";
    }

    public function skip_image() {
        $h = 180.0 * $this->cur_ratio;
        $ln = 0;

        if(!$this->dumphtml) {
            for($i = 0; $i < $h; $i += 4) {
                $ln .= "<br>";
            }

            $this->write($ln);
        }
    }

    public function note_start() {
        $this->content = "";
    }

    public function get_timestamp(): string {
        return sprintf("<h1>Time: %s</h1>", date("Y/m/d H:i:s"));
    }

    public function send(string $name) {
        $this->pdf->Output($name, 'F');
    }
}

function get_text_from_file(string $path): string {
    if(file_get_contents($path, FILE_USE_INCLUDE_PATH) === false) {
        echo "PHP file_get_contents error!";
    }

    return file_get_contents($path, FILE_USE_INCLUDE_PATH);
}


?>
