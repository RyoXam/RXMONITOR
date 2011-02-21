<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" media-type="text/html" indent="yes" />


<!--
	NIRVA config page for service XMONITOR
	This page is called as result of the SYSTEM SERVICE CONFIG command
-->


<xsl:template match="/">
	<html>
	<head>
	<style type="text/css">
		a:link
		{
			color: rgb(220,220,220); 
		}
		a:visited
		{
			color: rgb(220,220,220); 
		} 
		a:active 
		{
			color: rgb(240,240,240);
		}
		body
		{
			font-family:Geneva,Arial,Helvetica,sans-serif;
			font-size:12px;
			background-color: rgb(64,64,64);
			color: rgb(220,220,220);
		}
		table
		{
			font-family:Geneva,Arial,Helvetica,sans-serif;
			font-size:12px;
			table-border-color-light: rgb(102,153,255);
			table-border-color-dark: rgb(0,0,102); 
		}
		form, input, select, option
		{
			font-family:Geneva,Arial,Helvetica,sans-serif;
			font-size:12px;
		}
		h1, h2, h3, h4, h5, h6
		{
			font-family:Geneva,Arial,Helvetica,sans-serif;
		}
		h1
		{
			color: rgb(51,51,204);
		}
		h2
		{
			color: rgb(0,153,51); 
		}
		h3
		{
			color: rgb(204,51,51); 
		}
		h4
		{
			color: rgb(51,0,204);
		}
		h5
		{
			color: rgb(200,200,200);
		}
		h6
		{
			color: rgb(204,51,51);
		}

		table
		{
		}

		th
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
		}

		tr
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
		}

		td
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
		}

		table.table1
		{
			padding: 1px 5px 1px 5px;
		}

		th.table1
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
			color:white;
			background-color:#808080;
		}

		tr.table1
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
			color:black;
			background-color:#fafad8;
		}

		td.table1
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
			color:black;
		}
		a.table1:link
		{
			color: rgb(0,0,0); 
		}
		a.table1:visited
		{
			color: rgb(0,0,0); 
		} 
		a.table1:active 
		{
			color: rgb(0,0,0); 
		}

		table.form1
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
			color:black;
		}


		tr.form1
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
			color:black;
			background-color:#00ffff;
		}


		td.form1
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
			color:black;
			background-color:#00ffff;
		}

		tr.form1button
		{
			font-size:12px;
			font-family:Geneva,Arial,Helvetica,sans-serif;
			color:black;
			background-color:#ffffff;
		}


		table.contextbutton
		{
		}

		td.contextbutton
		{
			background:rgb(92,92,92);
		}

		a.contextbutton:link
		{
			font-family:Geneva,Arial,Helvetica,sans-serif;
			font-size:14px;
			text-decoration:none;
			color: rgb(255,255,255); 
		}
		a.contextbutton:visited
		{
			font-family:Geneva,Arial,Helvetica,sans-serif;
			font-size:14px;
			text-decoration:none;
			color: rgb(255,255,255); 
		} 
		a.contextbutton:active 
		{
			font-family:Geneva,Arial,Helvetica,sans-serif;
			font-size:14px;
			text-decoration:none;
			color: rgb(255,255,255); 
		}
		a.contextbutton:hover 
		{
			font-family:Geneva,Arial,Helvetica,sans-serif;
			font-size:14px;
			text-decoration:none;
			color: rgb(255,204,0);
		}
	</style>
	<title>NIRVA service xmonitor configuration</title>
	</head>
	<body>
	<xsl:apply-templates select="NIRVA"/>
	</body>
	</html>
</xsl:template>


<xsl:template match="NIRVA">
	<table bgcolor="#808080" width="100%">
		<tr>
			<td align="left">
				<table class="contextbutton" cellspacing="2" cellpadding="2">
					<tr>
						<td class="contextbutton">
						  	<a class="contextbutton" href="javascript:history.back()" title="Go back to the previous page">
								&#xA0;&#xA0;Back&#xA0;&#xA0;
							</a>
						</td>
						<td class="contextbutton">
						 	<a class="contextbutton" title="Service documentation" target="_blank">
								<xsl:attribute name="href">../../../../../../../../../../NV_DOC_SRV_xmonitor/Html/xmonitor.htm</xsl:attribute>
								&#xA0;&#xA0;Documentation&#xA0;&#xA0;
							</a>
						</td>
					</tr>
				</table>
			</td>
			<td align="right">
				<b>
					<font size="4">
						XMONITOR NIRVA service configuration
					</font>
				</b>
			</td>
		</tr>
	</table>
	<BR/>
	<p>
		<h5>
			TODO:
			<br/>
			This page is the starting page of the XMONITOR service configuration.
			<br/>
			Please modify it according your service configuration.
			<br/>
			This is an XSL page automatically called by NIRVA when choosing to configure the service.
			<br/>
			<br/>
			You can find this xsl page in the NIRVA XMONITOR Files/Config directory. The name of the file is config.xsl.
		</h5>
	</p>
</xsl:template>

</xsl:stylesheet>
