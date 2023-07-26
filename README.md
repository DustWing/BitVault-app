<h3 style="text-align: center;">
    BitVault
</h3>

<p style="text-align: center;">
    Simple password manager using Javafx
</p>

## Encryption
All username and password are saved using AES/GCM/NoPadding with key size 256.

The Master username and password are saved into the database using ARGON2d.

The records are saved in a sqlite database.

## Future goals
1. Sync all record with a mobile application, using QR code to start the local connection.
To start the local connection the user will scan the QR code that contains the port of the server that will
run in the desktop application (BitVault). The payload will be encrypted using the key provided by the local server. 
2. Allow other types of records to be saved. e.g. we should have the appropriate fields to allow card or other notes
3. More Encryption algorithm options
4. Setting for password generation/length

## Theme
Thanks to AtlantaFx for the nice theme
https://github.com/mkpaz/atlantafx

## Login Screen
![loginScreen](https://github.com/DustWing/BitVault-app/assets/24978428/5b419bef-d380-45fe-bdd8-80d7e6d06ffb)

## Passwords
![PasswordTable](https://github.com/DustWing/BitVault-app/assets/24978428/7e4f8a68-1bca-436e-9dee-12d9be09f053)

## Edit or Create password
![EditPassword](https://github.com/DustWing/BitVault-app/assets/24978428/662cbab1-341a-40ab-a408-a6bae40a2145)

## Edit or Create Category
![NewCategory](https://github.com/DustWing/BitVault-app/assets/24978428/9843b654-aacc-4c66-b439-2a24442a8e84)
