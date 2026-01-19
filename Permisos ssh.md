Usar llaves ssh

icacls "D:\Proyectos\FinanzasDash\FinanzasOracle.key" /inheritance:r
icacls "D:\Proyectos\FinanzasDash\FinanzasOracle.key" /remove "NT AUTHORITY\Authenticated Users"
icacls "D:\Proyectos\FinanzasDash\FinanzasOracle.key" /remove "BUILTIN\Usuarios"
icacls "D:\Proyectos\FinanzasDash\FinanzasOracle.key" /remove "BUILTIN\Administradores"
icacls "D:\Proyectos\FinanzasDash\FinanzasOracle.key" /remove "NT AUTHORITY\SYSTEM"
icacls "D:\Proyectos\FinanzasDash\FinanzasOracle.key" /grant:r "OBEDMASTERRACE\obedn:R"
