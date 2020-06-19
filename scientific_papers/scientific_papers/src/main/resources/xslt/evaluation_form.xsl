<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="https://github.com/AnaMijailovic/XML-Tim19"
                version="2.0">

    <xsl:template match="/">
        <html>
            <head>
                <title>Evaluation Form</title>
               
            </head>
            <body>
               	<div>
					<xsl:if test="ns:evaluation_form">
					<h3>Reviewer:</h3>
	       				<xsl:for-each select="ns:evaluation_form/ns:reviewer">       			
	               			<xsl:value-of select="concat(ns:first_name, ' ', ns:last_name)"/>
	               			<div></div>
	             			<xsl:value-of select="ns:email"/>
	             			<div></div>
	             			<div></div>
	               
	       				</xsl:for-each>
					</xsl:if>
				</div>
				
				
				<div>
	 	           <xsl:if test="ns:evaluation_form/ns:scientific_paper_title">
	                    <h3>Scientific paper title:</h3>                   		
	      				<xsl:value-of select="ns:evaluation_form/ns:scientific_paper_title"/>;
					</xsl:if>
				</div>
				
				
				<div>
	 	           <xsl:if test="ns:evaluation_form/ns:scientific_paper_summary">
	                    <h3>Scientific paper summary:</h3>                   		
	      				<xsl:value-of select="ns:evaluation_form/ns:scientific_paper_summary"/>;
					</xsl:if>
				</div>
		
		
			<div>
			<xsl:if test="ns:evaluation_form/ns:sugestions/ns:sugestion">
				<h3>Sugestions</h3>
				<xsl:for-each select="ns:evaluation_form/ns:sugestions/ns:sugestion"> 
	       			<p> 			
	               	<xsl:value-of select="ns:reg"/>
	       			</p>  
	       			<div></div>
	             	<xsl:value-of select="ns:comment"/>
				</xsl:for-each>           
			</xsl:if>
			</div>
			
			
			<div>
			<xsl:if test="ns:evaluation_form/ns:overall_recommendation">
				<h3>Overall recommendation</h3>
				<xsl:for-each select="ns:evaluation_form/ns:overall_recommendation"> 
	       			<p> 			
	               	<xsl:value-of select="ns:recommendation"/>
	       			</p>  
	       			<div></div>
	             	<xsl:value-of select="ns:comment"/>
				</xsl:for-each>           
			</xsl:if>
			</div>
			
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>