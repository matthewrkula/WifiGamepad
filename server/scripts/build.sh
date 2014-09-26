mkdir ../Server;
cp ./WifiGamepad\ Server.sh ../Server;
cp ../WifiGamepadServer/bin/*.class ../Server;
cd ..;
zip -r WifiGamepadServer.zip Server;
rm -rf Server;
