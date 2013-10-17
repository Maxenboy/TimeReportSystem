@echo off
if [%~1] == [] goto no_arg
if [%~2] == [] goto no_arg

set database=%~1
set sql_file=%~2
REM set mysql_bin_dir=..\..\..\mysql-5.5.13-win32\bin\
set mysql_bin_dir=
set db_user=puss1302
set db_password=jks78ww2

:again 
   echo This script will remove the current database and recreate it using a SQL file.
   echo.
   echo  Current database:  %database%
   echo  SQL file:          %sql_file%
   echo.
   set /p answer=Do you want to continue (Y/N)? 
   if /i "%answer:~,1%" EQU "Y" goto recreate
   if /i "%answer:~,1%" EQU "N" exit /b
   echo Please type Y for Yes or N for No
   goto again
   
:recreate
    @echo off
    echo.
    echo Removing current database...
    %mysql_bin_dir%mysql --user=%db_user% --password=%db_password% -e "DROP DATABASE %database%"
    echo.
    echo Creating new database...
    %mysql_bin_dir%mysql --user=%db_user% --password=%db_password% < %sql_file%
    echo.
    pause
    exit /b
    
:no_arg
    echo Error: No arguments were given. This script requires two arguments: database and SQL-file. Use the load_... .bat files instead.
    echo.
    pause
    exit /b