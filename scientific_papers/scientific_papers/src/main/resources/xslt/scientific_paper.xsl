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
                   				<xsl:for-each select="ns:scientific_paper/ns:head/ns:title">       			
               						<xsl:value-of select="."/>
               						
             						<div></div>	
             						</xsl:for-each>
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
	
		<div>Authors:</div>
       <xsl:for-each select="ns:scientific_paper/ns:head/ns:author">       			
               <xsl:value-of select="concat(ns:first_name, ' ', ns:last_name)"/>
               <div></div>
             	<xsl:value-of select="concat(ns:affiliation/ns:name, ', ', ns:affiliation/ns:city, ', ' , ns:affiliation/ns:country)"/>
             	<div></div>
             	<div></div>
               
       </xsl:for-each>
	</xsl:if>
				</div>
				
				<div>
                   <xsl:if test="ns:scientific_paper/ns:head/ns:keyword">
                        	<h4>                 		
								<xsl:value-of select="ns:scientific_paper/ns:head/ns:keyword"/>;
                    		</h4>
					</xsl:if>
				</div>
              
				
              	 <div>
                   <xsl:if test="ns:scientific_paper/ns:body/ns:abstract">
                       <h3>
                           		                        Abstract     				
                    		</h3>
                    		<p>
                    		<xsl:for-each select="ns:scientific_paper/ns:body/ns:abstract/ns:paragraph">       			
               						<xsl:value-of select="."/>
               						<div></div>
               
       						</xsl:for-each>                    		
                        </p>
					</xsl:if>
				</div>
	<div>
	<xsl:if test="ns:scientific_paper/ns:body/ns:chapter">
		<div  font-family="sans-serif">
		
       <xsl:for-each select="ns:scientific_paper/ns:body/ns:chapter"> 
       <h3> 			
               <xsl:value-of select="ns:heading"/>
       </h3>     
               <div></div>
             	<xsl:value-of select="ns:paragraph"/>
             	
             	
             	<div></div>
             	<div></div>
             	<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:image">
	    			<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:image"/>	
	   			</xsl:if>
	   			<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:table">						
	   				<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:table"/>	
	     		</xsl:if>
	     		<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:quote">						
	   				<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:quote"/>	
	     		</xsl:if>
	     		<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:unordered_list">						
	   				<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:unordered_list"/>	
	     		</xsl:if>
	     		<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:ordered_list">						
	   				<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:ordered_list"/>	
	     		</xsl:if>
	     		
               
       </xsl:for-each>
       </div>
	</xsl:if>
	</div>
	<div>
              <xsl:if test="ns:scientific_paper/ns:body/ns:references">
                        <div text-align-last="justify">
                           <h3>
                           		                        References     				
                    		</h3>
                    		<div></div>
							<xsl:for-each select="ns:scientific_paper/ns:body/ns:references/ns:reference">
               				<xsl:value-of select="."/>
            				<div></div>
             						
             							<div></div>
            				</xsl:for-each>                        
            				</div>
					</xsl:if>
				</div>
				
                  
			
            
                
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>