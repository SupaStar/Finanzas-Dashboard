Usar llaves ssh

icacls "C:\Users\obedn\.ssh\LocalServer" /inheritance:r
icacls "C:\Users\obedn\.ssh\LocalServer" /remove "NT AUTHORITY\Authenticated Users"
icacls "C:\Users\obedn\.ssh\LocalServer" /remove "BUILTIN\Usuarios"
icacls "C:\Users\obedn\.ssh\LocalServer" /remove "BUILTIN\Administradores"
icacls "C:\Users\obedn\.ssh\LocalServer" /remove "NT AUTHORITY\SYSTEM"
icacls "C:\Users\obedn\.ssh\LocalServer" /grant:r "OBEDMASTERRACE\obedn:R"
