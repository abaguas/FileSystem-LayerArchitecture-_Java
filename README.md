
  M Y D R I V E

  $ mysql -p -u root

  Enter password: rootroot

  mysql> GRANT ALL PRIVILEGES ON \*.\* TO 'myDrive'@'localhost' IDENTIFIED BY 'myDriv3' WITH GRANT OPTION;

  mysql> CREATE DATABASE drivedb;

  mysql> \q

  por https
  $ git clone https://github.com/tecnico-softeng/es16al_17-project.git
  ou por ssh
  $ git clone git@github.com:tecnico-softeng/es16al_17-project.git

  $ cd es16al_17-project.git

  $ mvn clean package exec:java

