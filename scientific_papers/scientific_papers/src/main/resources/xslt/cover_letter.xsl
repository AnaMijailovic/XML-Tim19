<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="https://github.com/AnaMijailovic/XML-Tim19"
                version="2.0">

    <xsl:template match="/">
        <html>
            <head>
                <title>Cover letter</title>
               
            </head>
            <body>
                 <div>
					<xsl:if test="ns:cover_letter">
						<h2>
	       				<xsl:for-each select="ns:cover_letter/ns:cover_letter_header">       			
	               			<xsl:value-of select="."/>
	               			<fo:block></fo:block>
	               		
	       				</xsl:for-each>
	       				</h2>
				
					</xsl:if>
				</div>
				
			<div>
			<xsl:if test="ns:cover_letter/ns:sender">
				<h3>Sender</h3>
				<xsl:for-each select="ns:cover_letter/ns:sender"> 
	       			<div> 			
	               	<xsl:value-of select="ns:sender_name"/>
	       			</div> 
	       			<div></div>
	             	<xsl:value-of select="ns:university_name"/>
	             	<div></div>
	            	<xsl:value-of select="ns:university_address"/>
	            	<div></div>
	             	<xsl:value-of select="ns:phone_number"/>
	             	<div></div>
	             	<xsl:value-of select="ns:email"/>
	             	<div></div>
				</xsl:for-each>           
			</xsl:if>
			</div>
			
			<div>
			<xsl:if test="ns:cover_letter/ns:recipient">
				<h3>Recipient</h3>
				<xsl:for-each select="ns:cover_letter/ns:recipient"> 
	       			<div>		
	               	<xsl:value-of select="ns:recipient_name"/>
	       			</div> 
	       			<div></div>
	             	<xsl:value-of select="ns:recipient_role"/>
	             	<div></div>
	            	<xsl:value-of select="ns:journal_name"/>
	            	<div></div>
				</xsl:for-each>           
			
			</xsl:if>
			</div>
				
	<div>
	 	           <xsl:if test="ns:cover_letter/ns:date">
	 	          		   <h3>Date:</h3>                   		
	      				<xsl:value-of select="ns:cover_letter/ns:date"/>;
	      				
					</xsl:if>
				</div>
				
				<div>
	 	           <xsl:if test="ns:cover_letter/ns:cover_letter_body">
	 	          		<div></div>                 		
	      				<xsl:value-of select="ns:cover_letter/ns:cover_letter_body"/>;
	      				
					</xsl:if>
				</div>
		
		<div>
			<xsl:if test="ns:cover_letter/ns:cover_letter_closing">
				<div></div>
				<xsl:for-each select="ns:cover_letter/ns:cover_letter_closing"> 
	       			<div> 			
	               	<xsl:value-of select="ns:sender_name"/>
	       			</div>  
	       			<div></div>
	             	<xsl:value-of select="ns:academic_title"/>
	             	<div></div>
	            	<xsl:value-of select="ns:department_name"/>
	            	<div></div>
	            	<xsl:value-of select="ns:university_name"/>
	            	<div></div>
				</xsl:for-each>           
			
			</xsl:if>
			</div>
		
				
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>