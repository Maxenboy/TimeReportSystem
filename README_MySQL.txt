# MySQL 5.5.13
# Denna README beskriver hur man sätter upp MySQL-databasen till systemet.
# Installationspaket finns på http://downloads.mysql.com/archives.php?p=mysql-5.5&v=5.5.13
# Installationsinstruktioner finns på http://dev.mysql.com/doc/refman/5.5/en/installing.html 

-------------------------------------------------------------------------------
# Kort guide för att installera med MSI-paket på Windows:
  http://downloads.mysql.com/archives/mysql-5.5/mysql-5.5.13-win32.msi
  Välj standardinstallation.
  Installera som service.
  Kryssa i att bin ska läggas till i PATH.
  Root-lösenord behöver inte sättas.

-------------------------------------------------------------------------------
# För att sätta upp MySQL för vårt system öppna kommandotolken och kör:
  mysql -u root
  CREATE USER 'puss1302'@'localhost' IDENTIFIED BY 'jks78ww2';
  CREATE DATABASE puss1302;
  USE puss1302
  GRANT ALL PRIVILEGES ON puss1302 . * TO 'puss1302'@'localhost';
# Tryck Ctrl+c eller skriv exit för att avsluta MySQL-prompten.

-------------------------------------------------------------------------------
# För att ta bort existerande databas och läsa in en ny:
# I katalogen db/windows finns .bat-filer för Windows för att snabbt tömma och läsa in en ny databas. Enklast är att dubbelklicka på dem för att köra dem.
# Det går dock lika bra att köra kommandona själv:
  mysql --user=puss1302 --password=jks78ww2 puss1302 -e "DROP DATABASE puss1302"
  mysql --user=puss1302 --password=jks78ww2 puss1302 < db/empty_database.sql