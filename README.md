I projektet finns en mapp "lib". I denna ligger tomcat-servern. Denna behöver flyttas lokalt och läggas någon annan stans. Sedan får man lägga till denna som server precis som på labben, handledning finns: http://cs.lth.se/fileadmin/serg/lab_lth_2013.pdf punkt tio. Ligger den i lib så går kommer det inte fungera. Ta dock inte bort mappen i lib utan kopiera denna lokalt. Ska försöka hitta en lösning för att låta oss alla ha lokala inställningar för detta som inte påverkar servern. Återkommer. 

Databas får sättas upp på vm-servern precis som på labben eller lokalt. Addressen som används till databasen finns i Database-konstruktorn och i dess testklass och kan behöva ändras om man har satt upp sin databas annorlunda. lib/setup_db.sql skapar databasen och tillhörande tabeller och kan importeras med tex. kommandot:

  mysql --user=puss1302 --password=jks78ww2 < lib/setup_db.sql

KÖR INTE DATABASTESTER OM NI HAR SAKER I DATABASEN SOM INTE FÅR FÖRSVINNA!

/UG1