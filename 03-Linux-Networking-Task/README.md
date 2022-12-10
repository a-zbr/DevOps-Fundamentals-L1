# 03-Linux-Networking-Task

## Linux Networking

***

1. На Server_1 налаштувати статичні адреси на всіх інтерфейсах.
2. На Server_1 налаштувати DHCP сервіс, який буде конфігурувати адреси Int1 Client_1 та Client_2
3. За допомогою команд ping та traceroute перевірити зв'язок між віртуальними машинами. Результат пояснити.
Увага! Для того, щоб з Client_1 та Client_2 проходили пакети в мережу Internet (точніше щоб повертались з Internet на Client_1 та Client_2) на Wi-Fi Router необхідно
налаштувати статичні маршрути для мереж Net2 та Net3. Якщо такої можливості немає інтерфейс Int1 на Server_1 перевести в режим NAT.
4. На віртуальному інтерфейсу lo Client_1 призначити дві ІР адреси за таким правилом: 172.17.D+10.1/24 та 172.17.D+20.1/24. Налаштувати маршрутизацію таким чином, щоб трафік з Client_2 до 172.17.D+10.1 проходив через Server_1, а до 172.17.D+20.1 через Net4. Для перевірки використати traceroute.
5. Розрахувати спільну адресу та маску (summarizing) адрес 172.17.D+10.1 та 172.17.D+20.1, при чому префікс має бути максимально можливим. Видалити маршрути, встановлені на попередньому кроці та замінити їх об’єднаним маршрутом, якій має проходити через Server_1.
6. Налаштувати SSH сервіс таким чином, щоб Client_1 та Client_2 могли підключатись до Server_1 та один до одного.
7. Налаштуйте на Server_1 firewall таким чином:

* Дозволено підключатись через SSH з Client_1 та заборонено з Client_2
* З Client_2 на 172.17.D+10.1 ping проходив, а на 172.17.D+20.1 не проходив.

8. Якщо в п.3 була налаштована маршрутизація для доступу Client_1 та Client_2 до мережі Інтернет – видалити відповідні записи. На Server_1 налаштувати NAT сервіс таким чином, щоб з Client_1 та Client_2 проходив ping в мережу Інтернет.

***

## Answers

***

### 1. На Server_1 налаштувати статичні адреси на всіх інтерфейсах

&emsp; 1.1 Configurations for task:

* Int1 IP address: 192.168.1.200/24;
* Net2 network address: 10.74.23.0/24;
* Net3 network address: 10.3.74.0/24;
* Net4 network address: 172.16.23.0/24.

***

&emsp; 1.2 Server_1 configuration:

* network configuration;
![net_00.1.PNG](./img/1-4/net_00.1.PNG)

* IP address configuration in /etc/netplan/00-installer-config.yaml;
![net_02.PNG](./img/1-4/net_02.PNG)

* system hostname configurations.
![net_05.PNG](./img/1-4/net_05.PNG)

***

&emsp; 1.3 Client_1 configuration:

* network configuration;
![net_00.2.PNG](./img/1-4/net_00.2.PNG)

* IP address configuration in /etc/netplan/00-installer-config.yaml;
![net_12.PNG](./img/1-4/net_12.PNG)

* system hostname configurations.
![net_11.PNG](./img/1-4/net_11.PNG)

***

&emsp; 1.4 Client_2 configuration:

* network configuration;
![net_00.3.PNG](./img/1-4/net_00.3.PNG)

* IP address configuration;
![net_00.PNG](./img/1-4/net_00.PNG)

* system hostname configurations.
![net_01.PNG](./img/1-4/net_01.PNG)

***

&emsp; 1.5 A new configuration was accepted on the Server_1.
![net_03.PNG](./img/1-4/net_03.PNG)
***

&emsp; 1.6 A new configuration was cheked on the Server_1.
![net_04.PNG](./img/1-4/net_04.PNG)
***

### 2. На Server_1 налаштувати DHCP сервіс, який буде конфігурувати адреси Int1 Client_1 та Client_2

&emsp; 2.1 A DHCP server has been installed on Server_1.
![net_06.PNG](./img/1-4/net_06.PNG)
***

&emsp; 2.2 Configuration of the dhcp.conf.
![net_07.PNG](./img/1-4/net_07.PNG)
***

&emsp; 2.3 Settings have been changed for two interfaces.
![net_08.PNG](./img/1-4/net_08.PNG)
***

&emsp; 2.4 DHCP server has been started.
![net_09.PNG](./img/1-4/net_09.PNG)
***

&emsp; 2.5 Restart network manager on the Client_2. Make "ping" to Server_1.
![net_10.PNG](./img/1-4/net_10.PNG)
***

&emsp; 2.6 Restart network manager on the Client_2. Make "ping" to Server_1.
![net_10.PNG](./img/1-4/net_10.PNG)
***

&emsp; 2.7 Netplan apply on the Server_1. Make "ping" to Server_1.
![net_13.PNG](./img/1-4/net_13.PNG)
***

&emsp; 2.8 Edit /etc/sysctl.conf file. Restart network manager on the Server_1.
![net_14.PNG](./img/1-4/net_14.PNG)
***

### 3. За допомогою команд ping та traceroute перевірити зв'язок між віртуальними машинами. Результат пояснити

Увага! Для того, щоб з Client_1 та Client_2 проходили пакети в мережу Internet (точніше щоб повертались з Internet на Client_1 та Client_2) на Wi-Fi Router необхідно
налаштувати статичні маршрути для мереж Net2 та Net3. Якщо такої можливості немає інтерфейс Int1 на Server_1 перевести в режим NAT.

&emsp; 3. Check the connection from Server_1 to Client_1 and Client_2.

* Server_1 -> Client_1
![net_15.PNG](./img/1-4/net_15.PNG)

* Client_1 -> Server_1
![net_16.PNG](./img/1-4/net_16.PNG)

***

### 4. На віртуальному інтерфейсу lo Client_1 призначити дві ІР адреси за таким правилом: 172.17.D+10.1/24 та 172.17.D+20.1/24. Налаштувати маршрутизацію таким чином, щоб трафік з Client_2 до 172.17.D+10.1 проходив через Server_1, а до 172.17.D+20.1 через Net4. Для перевірки використати traceroute

&emsp; 4.1 lo configuration on the Client_1:

* the first IP address: 172.17.33.1/24;
* the second IP address: 172.17.43.1/24;

***

&emsp; 4.2 Edit /etc/netplan/00-installer-config.yaml on the Client_1 to add 2 IP addresses.
![net_17.PNG](./img/1-4/net_17.PNG)
***

&emsp; 4.3 A new configuration was accepted on the Server_1.
![net_18.PNG](./img/1-4/net_18.PNG)
***

&emsp; 4.4 A new configuration was cheked on the Server_1.
![net_19.PNG](./img/1-4/net_19.PNG)
***

&emsp; 4.5 The route rule has been added.
![net_20.1.PNG](./img/1-4/net_20.1.PNG)
***

&emsp; 4.6 The route rule has been checked by using "ping" and "traceroute".
![net_20.2.PNG](./img/1-4/net_20.2.PNG)
***

&emsp; 4.7 The route rule has been added to the Server_1.
![net_21.PNG](./img/1-4/net_21.PNG)
***

&emsp; 4.8 The route rule has been checked by using "ping" and "traceroute" from the Client_2.
![net_22.PNG](./img/1-4/net_22.PNG)
***

### 5. Розрахувати спільну адресу та маску (summarizing) адрес 172.17.D+10.1 та 172.17.D+20.1, при чому префікс має бути максимально можливим. Видалити маршрути, встановлені на попередньому кроці та замінити їх об’єднаним маршрутом, якій має проходити через Server_1

&emsp; 5.1 The previous route rule has been deleted from the Server_1.
![net_23.PNG](./img/5/net_23.PNG)
***

&emsp; 5.2 The previous route rule has been deleted from the Client_1.
![net_24.PNG](./img/5/net_24.PNG)
***

&emsp; 5.3 Combined route calculation.

* 172.17.33.1 = 10101100.00010001.00100001 | 00000001
* 172.17.43.1 = 10101100.00010001.00101011 | 00000001
* Result: 172.17.32.0/20

***

&emsp; 5.4 The route rule has been added to the Server_1.
![net_25.2.PNG](./img/5/net_25.2.PNG)
***

&emsp; 5.5 The route rule has been added to the Server_1.
![net_25.2.PNG](./img/5/net_25.2.PNG)
***

&emsp; 5.6 The route rule has been checked by using "ping" and "traceroute" from the Client_2.
![net_26.PNG](./img/5/net_26.PNG)
***

### 6. Налаштувати SSH сервіс таким чином, щоб Client_1 та Client_2 могли підключатись до Server_1 та один до одного

&emsp; 6.1 SSH connection has been checked from Client_1 to Server_1.
![net_28.PNG](./img/6/net_28.PNG)
***

&emsp; 6.2 SSH connection has been checked from Client_2 to Server_1.
![net_27.PNG](./img/6/net_27.PNG)
***

&emsp; 6.3 SSH connection has been checked from Client_1 to Client_2.
![net_30.PNG](./img/6/net_30.PNG)
***

&emsp; 6.4 SSH connection has been checked from Client_2 to Client_1.
![net_29.PNG](./img/6/net_29.PNG)
***

### 7. Налаштуйте на Server_1 firewall таким чином:

* ### Дозволено підключатись через SSH з Client_1 та заборонено з Client_2

* ### З Client_2 на 172.17.D+10.1 ping проходив, а на 172.17.D+20.1 не проходив

&emsp; 7.1 lo interfaces:

* 172.17.33.1
* 172.17.43.1

&emsp; 7.2 The route rules have been added to the Server_1 in the iptables.
![net_31.PNG](./img/7/net_31.PNG)
***

&emsp; 7.3 SSH connection succeeded from Client_1 to Server_1.
![net_32.PNG](./img/7/net_32.PNG)
***

&emsp; 7.4 SSH connection failed from Client_2 to Server_1.
![net_33.PNG](./img/7/net_33.PNG)
***

&emsp; 7.5 The route rules have been added to the Server_1 to:

* accept ping from Client_2 to 172.17.33.1;
* drop ping from Client_2 to 172.17.43.1;
![net_34.PNG](./img/7/net_34.PNG)

***

&emsp; 7.6 The accept and drop rules have been checked.
![net_35.PNG](./img/7/net_35.PNG)
***

### 8. Якщо в п.3 була налаштована маршрутизація для доступу Client_1 та Client_2 до мережі Інтернет – видалити відповідні записи. На Server_1 налаштувати NAT сервіс таким чином, щоб з Client_1 та Client_2 проходив ping в мережу Інтернет

&emsp; 8.1 The NAT service was configured in the way that ping from Client_1 and Client_2 to the Internet could be performed.
![net_36.PNG](./img/8/net_36.PNG)
***

&emsp; 8.2 "ping" passes from Client_1 to the Internet.
![net_37.PNG](./img/8/net_37.PNG)
***

&emsp; 8.3 "ping" passes from Client_2 to the Internet.
![net_38.PNG](./img/8/net_38.PNG)
