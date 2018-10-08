
for %%i in (proto\*.proto) do (

	protoc --proto_path=proto --java_out=..\..\java\ %%i

)
pause