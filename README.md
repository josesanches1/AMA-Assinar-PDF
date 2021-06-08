Versão em java do projeto de Luís Abreu em C# -> https://github.com/luisabreu/AmaCmdSigning<br>
<b>Esta versão deve ser considerada como um ponto de partida - não está funcional e carece de melhoramentos</b>. 

Funcionalidades:<br>
	Obtém certificados utilizador<br>
	Cria assinatura vazia no PDF<br>
 	Obtém hash do PDF a assinar, com o prefixo da AMA<br>
	Cria PDF temporário com a assinatura vazia<br>
	Envia hash para AMA<br>
	AMA -> envio SMS<br>
	Validação do codigo (recebido via SMS)<br>
	Obtenção de hash assinada e inclusão da mesma e PDF.<br>
	Criação do PDF assinado.<br>
	Verificação da assinatura -> <b>falha</b>.<br>
	O erro é: "`O documento foi alterado ou corrompido após a aplicação da assinatura`"
	<br><br>

Necessário configurar as variaveis da classe: Config.java<br>
Dependências:<br>
	 	bcpkix-jdk15on-1.68.jar<br>
		bcprov-jdk15on-1.68.jar<br>
		commons-codec-1.15.jar<br>
		commons-io-2.6.jar<br>
		forms-7.1.15.jar<br>
		io-7.1.15.jar<br>
		kernel-7.1.15.jar<br>
		layout-7.1.15.jar<br>
		pdfa-7.1.15.jar<br>
		sign-7.1.15.jar<br>
		slf4j-api-1.7.30.jar<br>
<br><br><br>
<b><u>Agradece-se contributos</u></b>.
