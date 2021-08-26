<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<title>Registo Entidade</title>

<meta Http-Equiv="Cache-Control" Content="no-cache">
<meta Http-Equiv="Pragma" Content="no-cache">
<meta Http-Equiv="Expires" Content="0">
<meta Http-Equiv="Pragma-directive: no-cache">
<meta Http-Equiv="Cache-directive: no-cache">
<link rel="shortcut icon" href="../img/favicon.ico" >

<link rel="stylesheet" type="text/css" id="theme" href="../css/theme-default.css"/>
<link rel="stylesheet" href="../css/confirm/jquery-confirm.min.css">



<style type="text/css">
html, body {margin: 0;height: 100%;min-height: 100%;}
/*UPLOAD*/
.box{font-size: 1.25rem; background-color: #c8dadf;position: relative;height: 500px;padding-top:180px;}
.box.has-advanced-upload{outline: 2px dashed #92b0b3;outline-offset: -10px;-webkit-transition: outline-offset .15s ease-in-out, background-color .15s linear;transition: outline-offset .15s ease-in-out, background-color .15s linear;}
.box.is-dragover{outline-offset: -20px;outline-color: #c8dadf;background-color: #fff;}
.box__dragndrop,
.box__icon{display: none;}
.box.has-advanced-upload .box__dragndrop{display: inline;}
.box.has-advanced-upload .box__icon{width: 100%;height: 80px;fill: #92b0b3;display: block;margin-bottom: 40px;}
.box.is-uploading .box__input,
.box.is-success .box__input,
.box.is-error .box__input{visibility: hidden;}
.box__uploading {display: none;}
.box.is-uploading .box__uploading,
.box.is-success 
.box.is-error {display: block;position: absolute;top: 50%;right: 0;left: 0;-webkit-transform: translateY( -50% );transform: translateY( -50% );}
.box__uploading{font-style: italic;}
/*.box__success{-webkit-animation: appear-from-inside .25s ease-in-out;animation: appear-from-inside .25s ease-in-out;}*/
@-webkit-keyframes appear-from-inside{
from	{ -webkit-transform: translateY( -50% ) scale( 0 ); }
75%		{ -webkit-transform: translateY( -50% ) scale( 1.1 ); }
to		{ -webkit-transform: translateY( -50% ) scale( 1 ); }
}
@keyframes appear-from-inside{
from	{ transform: translateY( -50% ) scale( 0 ); }
75%		{ transform: translateY( -50% ) scale( 1.1 ); }
to		{ transform: translateY( -50% ) scale( 1 ); }
}
.box__restart{font-weight: 700;}
.box__restart:focus,
.box__restart:hover{color: #39bfd3;}

.js .box__file{width: 0.1px;height: 0.1px;opacity: 0;overflow: hidden;position: absolute;z-index: -1;}
.js .box__file + label{max-width: 80%;text-overflow: ellipsis;white-space: nowrap;cursor: pointer;display: inline-block;overflow: hidden;}
.js .box__file + label:hover strong,
.box__file:focus + label strong,
.box__file.has-focus + label strong{color: #39bfd3;}
.js .box__file:focus + label,
.js .box__file.has-focus + label{outline: 1px dotted #000;outline: -webkit-focus-ring-color auto 5px;}
.js .box__file + label *{}
.no-js .box__file + label{display: none;}
.no-js .box__button{display: block;}
.box__button{font-weight: 700;color: #e5edf1;background-color: #39bfd3;display: none;padding: 8px 16px;margin: 40px auto 0;}
.box__button:hover,
.box__button:focus{background-color: #0f3c4b;}
.box__file {width: 0.1px;height: 0.1px;opacity: 0;overflow: hidden;position: absolute;z-index: -1;}
#the-canvas {border: 1px solid black;direction: ltr;}
</style>

</head>
<body class="page-container-boxed">
	<form method="post" action="../../UploadCSV" enctype="multipart/form-data" class="box">
	    <div class="box__input" style="text-align: center;flex: auto;vertical-align: middle;border:1px dashed gray;min-height: 100%;max-height: 100%;width: 1200px;margin:auto;">
			<svg class="box__icon" xmlns="http://www.w3.org/2000/svg" width="50" height="43" viewBox="0 0 50 43">
				<path d="M48.4 26.5c-.9 0-1.7.7-1.7 1.7v11.6h-43.3v-11.6c0-.9-.7-1.7-1.7-1.7s-1.7.7-1.7 1.7v13.2c0 .9.7 1.7 1.7 1.7h46.7c.9 0 1.7-.7 1.7-1.7v-13.2c0-1-.7-1.7-1.7-1.7zm-24.5 6.1c.3.3.8.5 1.2.5.4 0 .9-.2 1.2-.5l10-11.6c.7-.7.7-1.7 0-2.4s-1.7-.7-2.4 0l-7.1 8.3v-25.3c0-.9-.7-1.7-1.7-1.7s-1.7.7-1.7 1.7v25.3l-7.1-8.3c-.7-.7-1.7-.7-2.4 0s-.7 1.7 0 2.4l10 11.6z"/>
			</svg>
			<input type="file" name="fileCSV" id="fileCSV" class="box__file" />
			<label for="fileCSV"><strong style="cursor: pointer;">Selecione um ficheiro</strong><span class="box__dragndrop"> ou arraste-o para aqui</span>.</label>
			<button type="submit" class="box__button">Upload</button>
		</div>
		<div class="box__uploading" style="text-align: center">Por favor aguarde. A processar a informação&hellip;</div>
	</form>
	<div style="display:none">
		<div class="field" style="width: 180px;">
			Pessoa contacto: <div style="display:inline;color:red;">*</div><br>
			<input class="form-control obrigatorio" style="width:100%;float:left;" id="pContacto" name="pContacto" maxlength="50" type="text" readonly="readonly">
		</div>
		<br style="clear:both;">
		<div>
		  	<button id="prev">Anterior</button>
		  	<button id="next">Próximo</button>
 				&nbsp; &nbsp;
 				<span>Página: <span id="page_num"></span> / <span id="page_count"></span></span>
		</div>
		<canvas id="the-canvas"></canvas>
	</div>
	
	



    <!-- >div class="page-container">
        <div class="page-content" style="margin-left:0px;">
			<div class="page-title">                    
            	<h2><span class="fa"></span> Assinar PDF</h2>
			</div>                   
			<div class="page-content-wrap">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-default" style="flex: auto;">
							<div class="panel-body">
								<div class="box__input" style="text-align: center;flex: auto;">
									<svg class="box__icon" xmlns="http://www.w3.org/2000/svg" width="50" height="43" viewBox="0 0 50 43">
										<path d="M48.4 26.5c-.9 0-1.7.7-1.7 1.7v11.6h-43.3v-11.6c0-.9-.7-1.7-1.7-1.7s-1.7.7-1.7 1.7v13.2c0 .9.7 1.7 1.7 1.7h46.7c.9 0 1.7-.7 1.7-1.7v-13.2c0-1-.7-1.7-1.7-1.7zm-24.5 6.1c.3.3.8.5 1.2.5.4 0 .9-.2 1.2-.5l10-11.6c.7-.7.7-1.7 0-2.4s-1.7-.7-2.4 0l-7.1 8.3v-25.3c0-.9-.7-1.7-1.7-1.7s-1.7.7-1.7 1.7v25.3l-7.1-8.3c-.7-.7-1.7-.7-2.4 0s-.7 1.7 0 2.4l10 11.6z"/>
									</svg>
									<input type="file" name="fileCSV" id="fileCSV" class="box__file" />
									<label for="fileCSV"><strong style="cursor: pointer;">Selecione um ficheiro</strong><span class="box__dragndrop"> ou arraste-o para aqui</span>.</label>
									<button type="submit" class="box__button">Upload</button>
								</div>
								<div class="box__uploading" style="text-align: center">Por favor aguarde. A processar a informação&hellip;</div>
							
								<div style="display:none">
									<div class="field" style="width: 180px;">
										Pessoa contacto: <div style="display:inline;color:red;">*</div><br>
										<input class="form-control obrigatorio" style="width:100%;float:left;" id="pContacto" name="pContacto" maxlength="50" type="text" readonly="readonly">
									</div>
									<br style="clear:both;">
									<div>
									  	<button id="prev">Anterior</button>
									  	<button id="next">Próximo</button>
						  				&nbsp; &nbsp;
						  				<span>Página: <span id="page_num"></span> / <span id="page_count"></span></span>
									</div>
									<canvas id="the-canvas"></canvas>
								</div>
							</div>
						</div>
					</div>
			     </div>
			 </div>
		</div>            
	</div>
<!-- START PLUGINS -->
<script type="text/javascript" src="../js/plugins/jquery/jquery.min.js"></script>
<script type="text/javascript" src="../js/plugins/blockUI/jquery-blockUI.js"></script>
<script type="text/javascript" src="../js/plugins/confirm/jquery-confirm.min.js"></script>
<script type="text/javascript" src="../js/plugins/imagearea/jquery.imgareaselect.dev.js"></script>
<!-- script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.5.207/pdf.js"> </script-->
<script src="//mozilla.github.io/pdf.js/build/pdf.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	var isAdvancedUpload = function(){
		var div = document.createElement( 'div' );
		return ( ( 'draggable' in div ) || ( 'ondragstart' in div && 'ondrop' in div ) ) && 'FormData' in window && 'FileReader' in window;
	}();
	
	// applying the effect for every form
	$( '.box' ).each( function(){
		var $form		 = $( this ),
			$input		 = $form.find( 'input[type="file"]' ),
			$label		 = $form.find( 'label' ),
			$restart	 = $form.find( '.box__restart' ),
			droppedFiles = false,
			showFiles	 = function( files ){
				$label.text('Por favor, aguarde...');
			};

		//tipo de ação
		//$form.append( '<input type="hidden" name="aca" value="1" />' );
		//O ficheiro é submetido de forma automática 
		$input.on( 'change', function( e ){
			showFiles( e.target.fileCSV );
			$form.trigger( 'submit' );
		});


		// drag&drop files if the feature is available
		if( isAdvancedUpload ){
			$form
				.addClass( 'has-advanced-upload' ) // letting the CSS part to know drag&drop is supported by the browser
				.on( 'drag dragstart dragend dragover dragenter dragleave drop', function( e ){
					// preventing the unwanted behaviours
					e.preventDefault();
					e.stopPropagation();
				})
			.on( 'dragover dragenter', function(){
				$form.addClass( 'is-dragover' );
			})
			.on( 'dragleave dragend drop', function(){
				$form.removeClass( 'is-dragover' );
			})
			.on( 'drop', function( e ){
				droppedFiles = e.originalEvent.dataTransfer.files; // the files that were dropped
				showFiles( droppedFiles );
				$form.trigger( 'submit' ); // automatically submit the form on file drop
			});
		}
		// Se o formulário for submetido
		$form.on( 'submit', function( e ){
			var file=droppedFiles?droppedFiles[0]:e.target.fileCSV.files[0];			

			if(file.type!="application/pdf"){
				$.alert({useBootstrap:false,boxWidth:'380px',title:'Atenção:',content: "O tipo de ficheiro não é <b>pdf</pdf>."});
				$label.html('<strong>Selecione um ficheiro</strong><span class="box__dragndrop"> ou arraste-o para aqui</span>.');
				$form.removeClass( 'is-uploading' );
				droppedFiles=false;
				return false;
			}
			
			var ext=file.name.split(".")[1];
			if(null===ext || typeof ext=="undefined" || ext.toLowerCase()!="pdf"){
				$.alert({useBootstrap:false,boxWidth:'380px',title:'Erro:',content: "Extensão do ficheiro desconhecida. Espera-se <b>pdf</b>."});
				$label.html('<strong>Selecione um ficheiro</strong><span class="box__dragndrop"> ou arraste-o para aqui</span>.');
				$form.removeClass( 'is-uploading' );
				droppedFiles=false;
				return false;
			}
			if(file.size>5242880){//5 MB
				$.alert({useBootstrap:false,boxWidth:'380px',title:'Erro:',content: "O ficheiro que adicionou é superior a 5 MB."});
				$label.html('<strong>Selecione um ficheiro</strong><span class="box__dragndrop"> ou arraste-o para aqui</span>.');
				$form.removeClass( 'is-uploading' );
				droppedFiles=false;
				return false;
			}
			
			// previne multiplos upload's. Bloqueia se estiver a decorrer um upload.
			if( $form.hasClass( 'is-uploading' ) ) return false;
			$form.addClass( 'is-uploading' ).removeClass( 'is-error' );
			// ajax file upload for modern browsers
			if( isAdvancedUpload ){
				e.preventDefault();
				// reunindo os dados do formulário
				//var ajaxData = new FormData( $form.get( 0 ) );
				var ajaxData = new FormData();
				ajaxData.append("aca", ACAO);				
				if(typeof file !== typeof undefined && null!=file && file.size>0){
					ajaxData.append("fileCSV", file);
				}else{
					$.alert({useBootstrap:false,boxWidth:'380px',title:'Erro:',content: "Problemas internos no browser no processamento do ficheiro.<br><br>Tente novamente."});
					$label.html('<strong>Selecione um ficheiro</strong><span class="box__dragndrop"> ou arraste-o para aqui</span>.');
					$form.removeClass( 'is-uploading' );
					droppedFiles=false;
					return false;
				}
				_JSON=null;
				$("#btErrorDownload").hide();
				$("#btNovoUpload").hide();
				
				// ajax request
				//Ativa o pdf
			}else{
				// fallback Ajax solution upload for older browsers
				var iframeName	= 'uploadiframe' + new Date().getTime(),
					$iframe		= $( '<iframe name="' + iframeName + '" style="display: none;"></iframe>' );
				$( 'body' ).append( $iframe );
				$form.attr( 'target', iframeName );

				$iframe.one( 'load', function(){
					var data = $.parseJSON( $iframe.contents().find( 'body' ).text() );
					$form.removeClass( 'is-uploading' );
					alert("OK");
					$iframe.remove();
				});
			}
		});
	});
});
/*******************************
 * OUTRAS DIMENSÕES DO BROWSER *
 *******************************/
$(window).on("resize", function () {setResizeBrowser()});
function setResizeBrowser(){
	if($(window).width() < 1200){
        if($("body").hasClass("page-container-boxed")){
            $("body").removeClass("page-container-boxed").data("boxed","1");
        }
    }else{
        if($("body").data("boxed") === "1"){
            $("body").addClass("page-container-boxed").data("boxed","");
        }
    }
}
/*********************
 * Tratamento do PDF *
 *********************/
var _tipoAcao=null;
var _areaSlect=null;
var pageNum = 1;
var canvas = document.getElementById('the-canvas');
var ctx = canvas.getContext('2d');
var pdfjsLib = window['pdfjs-dist/build/pdf'];

//The workerSrc property shall be specified.
pdfjsLib.GlobalWorkerOptions.workerSrc = '//mozilla.github.io/pdf.js/build/pdf.worker.js';

//********** DEFINIR E CARREGAR O PDF **********
var _altura = null;
var _totalPaginas = 0;
var _escalaPDF = 0
function setPDF() {
	_altura = Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0);
	if (_altura > (842 + 21 + 2)) _altura = 842 + 21 + 2;

	$("#divPrincipal").hide();
	$("body").css("padding", "3px 0px 3px 0px");
	$("div#contentorPDF").css({"height": (_altura-12) + "px"}).show();
	$("div#divLateral").css({"height": (_altura-21) + "px"});

	//http://www.imprimerie-pau.com/Upload-Ajax-ABCI/Javascript/jquery.imgareaselect-10-0-rc-1/imgAreaSelect_Documentation.html
	_areaSlect=$('#the-canvas').imgAreaSelect({ 
		instance: true,
		handles: false,
        disable:true,
        enable:false,
        onSelectEnd: function (img, selection) {
//console.log(JSON.stringify(selection));
        	if(_tipoAcao=="assinatura") INFO.assinatura=selection;
   	        if(_tipoAcao=="nrSeq") INFO.nrRegisto=selection;
   	        if(_tipoAcao=="data") INFO.data=selection;
		}
	});
	renderPDF(1);
}
function renderPDF(numPag){
var file = document.getElementById("versao1").files[0];
var fileReader = new FileReader();
	
	fileReader.onload = function() {
		$.unblockUI();
    	var typedarray = new Uint8Array(this.result);
	    const loadingTask = pdfjsLib.getDocument(typedarray);
    	loadingTask.promise.then(pdf => {

	      // The document is loaded here...
    	  //This below is just for demonstration purposes showing that it works with the moderen api
		pdf.getPage(pageNum).then(function(page) {
	        var canvas = document.getElementById('the-canvas');
	        var context = canvas.getContext('2d');
	        var viewport = page.getViewport({scale: 1});
	        var vH=viewport.height;
	        _escalaPDF = (_altura - 21) / viewport.height;
			
			viewport = page.getViewport({scale: _escalaPDF});
			var h=_escalaPDF * vH;
		
	        canvas.height = h;
	        canvas.width = viewport.width;
	        
	      	//inclusão das caixas
	      	_totalPaginas = pdf.numPages;
	      	document.getElementById('page_count').textContent = _totalPaginas;
	    	_setCaixaAssinatura(pageNum);

        	// Render PDF page into canvas context
	        var renderContext = {
				canvasContext: context,
				viewport: viewport
	        };
	        var renderTask = page.render(renderContext);
	        renderTask.promise.then(function() {
	        });

      	});
      //end of example code
    });
  }
  fileReader.readAsArrayBuffer(file);
  // Update page counters
  document.getElementById('page_num').textContent = pageNum;
}
//pagina seguinte
function onPrevPage() {
  	if (pageNum <= 1) return;
  	$('div._PDFcaixa').remove();
  	pageNum--;
  	renderPDF(pageNum);
}
/**
 * Displays next page.
 */
function onNextPage() {
  if (pageNum >= _totalPaginas) return;
  $('div._PDFcaixa').remove();
  pageNum++;
  renderPDF(pageNum);
}
document.getElementById('prev').addEventListener('click', onPrevPage);
document.getElementById('next').addEventListener('click', onNextPage);
</script>
</body>
</html>