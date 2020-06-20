Predmetni projekat iz premdeta XML i veb servisi

Tim 19:
 - Dušan Stević SW-10/2016
 - Ana Mijailović SW1-3/2016
 - Kristina Đereg SW-36/2016
 - Milica Vojnović SW-77/2016


 Uputstvo za pokretanje:

  - Pokretanje frontend-a
      - Pozicionirati se u frontend
      - Sledećim redosledom izvršiti komande:
        - npm install
        - ng serve
      - Otvoriti http://localhost:4200/ u browser-u
        
  - Pokretanje backend-a:
    -  Pokretanje java aplikacije:
        - Pozicionirati se u scientific_papers
        - Pokrenuti fajl `\src\main\java\com\ftn\scientific_papers\ScientificPapersApplicationjava` 
    -  Pokretanje Apache Jena Fuseki baze podataka
        - Skinuti Apache Jena Fuseki bazu podataka sa [Apache Jena 3.14.0](https://www-eu.apache.org/dist/jena/binaries/apache-jena-3.14.0.zip)
        - Otpakovati je i pozicionirati se unutar njenog foldera
        - Pokrenuti `.\bin\startup.bat` fajl
        - Proveriti da li je aplikacije pokrenuta odlaskom na [Apache Jena Fuseki](http://localhost:8080/fuseki)
    - Pokretanje Exist baze podataka 
        - Skinuti Exist bazu podataka sa [Exist 5.2.0](https://bintray.com/existdb/releases/download_file?file_path=exist-distribution-5.2.0-win.zip)
        - Pokrenuti `.\bin\startup.bat` fajl
        - Proveriti da li je aplikacija pokrenuta odlaskom na [Exist](http://localhost:8080/exist/apps/eXide/)

- Video: 

  https://drive.google.com/file/d/1dkC_oEGE5Fwcq0YY4TiR3M9Rq2iwQ1gS/view?usp=sharing      

