// xmonitor.dsc : description file
// NIRVA service
// This file should reside in the NIRVA/Services/XMONITOR/Files directory

// This file contains the XMONITOR NIRVA service description
// NIRVA tries to read it when loading the service
// The xmonitor.dsc file should be installed in the service File directory
// This file is not required but is very usefull for NIRVA configuration and for error
// reporting



// INFO section
// The INFO section gives some general service information on the form infoname = info value 
// Any new string can be added, removed or modified
[INFO]
VERSION = 1.00
DESCRIPTION = XMONITOR NIRVA service
COMPANY =
COPYRIGHT =



// SETTINGS section
// The INFO section gives some general service paramters on the form param name = param value 
// Any new string can be added, removed or modified
[SETTINGS]
LANGUAGE = JAVA
PATH = 


// PERMISSIONS section
// This section enumerates the XMONITOR security permissions on the form permissionname = permissiondescription
// If the security permissions are not used by the service, this section can be removed or let empty
[PERMISSIONS]




// COMMAND_CLASSES section
// This section enumerates the XMONITOR command classes on the form classname = class description
// If the class names are not used by the service, this section can be removed or let empty
[COMMAND_CLASSES]
XMONITOR = default service command class



// COMMAND_CLASS sections
// This section enumerates the XMONITOR commands of one class on the form commandname = command description
// Each section itself has the form [COMMAND_CLASS_classname] where classname is the name of the class
// as defined in the COMMAND_CLASSES section
[COMMAND_CLASS_XMONITOR]
NOP = No operation


// ERROR_CLASSES section
// This section enumerates the XMONITOR error classes on the form classname = class description
// If the class names are not used by the service, this section can be removed or let empty
[ERROR_CLASSES]
XMONITOR = default service error class


// ERROR_LANGUAGES section
// This section enumerates the error languages of the error descriptions given in the ERROR CLASS sections
// This is a succession of language strings. Valid languages are ENGLISH, FRENCH, GERMAN, ITALIAN and SPANISH
// Any other language can be added.
// The language order must correspond to the description order given in the ERROR_CLASS sections
[ERROR_LANGUAGES]
ENGLISH
FRENCH
GERMAN
ITALIAN
SPANISH



// ERROR_CLASS sections
// This section enumerates the XMONITOR errors of one class on the form 
// errorcode = error description language 1;error description language 2;etc..
// The error description language order must correspond to the order given in the ERROR_LANGUAGES section.
// Each section itself has the form [ERROR_CLASS_classname] where classname is the name of the class
// as defined in the ERROR_CLASSES section
[ERROR_CLASS_XMONITOR]
0 = No error;pas d'erreur;Keine Störung;Nessun errore;Ningún error

