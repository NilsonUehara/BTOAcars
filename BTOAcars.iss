[Files]
Source: .\instalador\apt; DestDir: {app}
Source: .\instalador\acft; DestDir: {app}
Source: .\instalador\BTOAcars.bat; DestDir: {app}
Source: .\instalador\cfg.txt; DestDir: {app}; Flags: onlyifdoesntexist
Source: .\instalador\BTOAcars.jar; DestDir: {app}
Source: .\instalador\fsuipc_java.dll; DestDir: {app}
Source: .\instalador\LeiaMe.pdf; DestDir: {app}; Flags: isreadme
Source: .\instalador\splash.png; DestDir: {app}
Source: .\instalador\bto.png; DestDir: {app}
Source: .\instalador\lib\fsuipc.jar; DestDir: {app}\lib
Source: .\instalador\lib\nimbus-snapshot-20090514.jar; DestDir: {app}\lib
Source: .\instalador\lib\xstream-1.3.1.jar; DestDir: {app}\lib
Source: .\instalador\boteco.ico; DestDir: {app}
[Icons]
Name: {group}\BTOAcars; Filename: {app}\BTOAcars.jar; WorkingDir: {app}; IconFilename: {app}\boteco.ico; IconIndex: 0
[Setup]
OutputDir=.
OutputBaseFilename=BTOAcars
VersionInfoVersion=1.24
VersionInfoCompany=Aeroboteco
VersionInfoDescription=Sistema de coleta de dados de voo
AppCopyright=Nilson Uehara
AppName=BTOAcars
AppVerName=BTOAcars 1.24
DefaultDirName=\BTOAcars
PrivilegesRequired=none
DefaultGroupName=BTOAcars
AppSupportURL=http://www.aeroboteco.com.br/forum/index.php?board=59.0
UninstallDisplayIcon={app}\boteco.ico
UninstallDisplayName=Desinstalar BTOAcars
AppID={{6FCA23D7-5F85-4177-A014-34A93BA9DB7B}
SetupIconFile=.\instalador\boteco.ico
