<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="https://github.com/AnaMijailovic/XML-Tim19"
                version="2.0">

    <xsl:template match="/">
        <html>
            <head>
                <title>Scientific Paper</title>
               
            </head>
            <body>
              
                 <div style="width:800px; margin:0 auto;">
                   <xsl:if test="ns:scientific_paper/ns:head/ns:title">
                   				<h1>
                   				<xsl:value-of select="ns:scientific_paper/ns:head/ns:title"/>	
                   				</h1>
					</xsl:if>
				</div>
				
				<div style="width:800px; margin:0 auto;">
				 <xsl:if test="ns:scientific_paper/ns:head">
				 				<p>
                 		 		Recieved date: 
                   				<xsl:value-of select="ns:scientific_paper/ns:head/ns:recieved_date"/>
                   				</p>
                   				<p>
                   				Revised date:
                    			<xsl:value-of select="ns:scientific_paper/ns:head/ns:revised_date"/>	
                    			</p>
                    			<p>
                    			Accepted date:
                    			<xsl:value-of select="ns:scientific_paper/ns:head/ns:accepted_date"/>	
                    			</p>
					</xsl:if>
				</div>
				
				<div style="width:800px; margin:0 auto;">
                   <xsl:if test="ns:scientific_paper/ns:head/ns:author">   		
                   			<xsl:for-each select="ns:scientific_paper/ns:head/ns:author/ns:first_name">
            				<xsl:value-of select="."/>;
            				
            				</xsl:for-each>
            				<p></p>
            				<xsl:for-each select="ns:scientific_paper/ns:head/ns:author/ns:last_name">
            				<xsl:value-of select="."/>;
            				
            				</xsl:for-each>
            				
            				<p></p>
            				<xsl:for-each select="ns:scientific_paper/ns:head/ns:author/ns:affiliation">
            				<xsl:value-of select="."/>;
            				
            				</xsl:for-each>
            		
                   
					</xsl:if>
				</div>
				
				<div>
                   <xsl:if test="ns:scientific_paper/ns:head/ns:keyword">
                        	<h4>                 		
                    		<xsl:for-each select="ns:scientific_paper/ns:head/ns:keyword">
            				<xsl:value-of select="."/>;
            				</xsl:for-each>
                    		</h4>
					</xsl:if>
				</div>
              
				
              	 <div>
                   <xsl:if test="ns:scientific_paper/ns:body/ns:abstract">
                       <h3>
                           		                        Abstract     				
                    		</h3>
                    		<p>
                    		<xsl:for-each select="ns:scientific_paper/ns:body/ns:abstract">
            				<xsl:value-of select="."/>;
            				</xsl:for-each>
                        </p>
					</xsl:if>
				</div>
              
              <div>
                   <xsl:if test="ns:scientific_paper/ns:body/ns:chapter">
                        <h3>
                           	<xsl:for-each select="ns:scientific_paper/ns:body/ns:chapter/ns:heading">
                           	<div></div>
            				<xsl:value-of select="."/>;
            				</xsl:for-each>
                        </h3>
                        <p>
  	                			<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph"/>	
  	                	</p>
  	                	<p>
     							<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:image">
	     							<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:image"/>
	     							<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:image/ns:name"/>	
	     							<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:image/ns:description"/>	
	     						</xsl:if>
	     				</p>
	     				<p>
	     						<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:table">
	     							<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:table"/>
	     							<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:table/ns:name"/>	
	     							<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:table/ns:description"/>	
	     						</xsl:if>
	     				</p>			
                   
					</xsl:if>
				</div>
              
               <div>
                   <xsl:if test="ns:scientific_paper/ns:body/ns:references">
                       <h3>
                           		                        References     				
                    		</h3>
                    		<p>
                    		
                    		<xsl:for-each select="ns:scientific_paper/ns:body/ns:references/ns:reference">
                    		<div></div>
            				<xsl:value-of select="."/>;
            				</xsl:for-each>
                        </p>
					</xsl:if>
				</div>
				
          
				
                  
			
            
                
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>