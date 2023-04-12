# CryptoJava

An cryptography software based on Java Swing 


[![Made-In-Senegal](https://github.com/GalsenDev221/made.in.senegal/blob/master/assets/badge.svg)](https://github.com/GalsenDev221/made.in.senegal)

## Installation

You must have jdbc mysql connector and create database(cryptojava) with this command at the root of project.

```bash
database credentials
database name : cryptojava
root password : Ir00t@dmin12
```

you can change it,it's in [ConnexionDb file](./src/ahmadouBambaDiagne/bdd/ConnexionDB.java)

```bash
mysql -u username -p cryptojava < cryptojava.sql
```

## Usage

With this tool, you can generate your own keys,crypt text or document,sign text or document.You can also convert your keys file or document to base64 string.

## Project parts

#### Account creation

In this part ,you need to create account to access others parts of app

<span style="display:block">
<img src="./images/1.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" />

Click on signup(inscription) button and then create account

<img src="./images/2.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/3.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

After create account ,you can login into system and have access to others parts

<img src="./images/4.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/5.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

#### Views of differents features of apps

<img src="./images/6.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

#### Key Generation

In this part,you will be able to generate symetrics key(AES,DES) and asymetric keys(RSA,DSA).

##### Symetric key

For example ,generate AES 256 key size process
<img src="./images/7.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/8.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/9.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/10.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

It's same process for asymetric key generation

#### Encryption/Decryption

In this part,you will be able to encrypt or decrypt documents or text with symetrics algorithm(AES,DES) and asymetric algorithm(RSA).
<img src="./images/11.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" />

Ok,in this example i will encrypt an text file with symetric algorithm
<img src="./images/12.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/13.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/14.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/15.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

Decrypt process

<img src="./images/16.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/17.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/18.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/19.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/20.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/21.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/><br/><br/>

#### Signature/Verification

In this part,you will be able to sign documents or verify documents, texts signature with some algorithms(SHA256WITHRSA,DSA).<br/><br/>

In this example,I will sign an text file with SHA256WITHRSA with private key.

<img src="./images/22.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/23.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/24.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>  
<img src="./images/25.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/26.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

After,we can verify file signature with public key.
<img src="./images/27.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/28.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>
<img src="./images/29.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>  
<img src="./images/30.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/31.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

<img src="./images/32.png"
     alt="Markdown Monster icon"
     style="float: left; margin-right: 10px;" /><br/><br/>

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
