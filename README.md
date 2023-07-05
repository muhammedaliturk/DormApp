# DormApp
Automation application for student dormitories

student using DormApp;

Will be able to see the announcements/events and indicate whether or not to participate in the events in the form of voting,

will be able to see the occupancy status of the existing washing and drying machines in the dormitory.

If the machines are full, they will be able to see who is using them or how many minutes until the machine program is finished.

will be able to see the food list in the cafeteria in the form of weekly and monthly tables.

will be able to pay dormitory fees with virtual POS.

will be able to communicate with the administrator through the application.

will be able to get permission out through the app.

The visual parts of the application will become more beautiful in the future. Right now I'm just concentrating on the backend part.

Login screen

![photo_2023-07-06_00-04-10](https://github.com/muhammedaliturk/DormApp/assets/103900615/42e4fdd4-3339-4218-9698-389252f13bb1)



i am using firebase for authentication on login screen. Application asks for institution id, email and password on first login.
In the next logins, the institution id and email will come automatically, just enter the password.
If you want, you can use fingerprint. 

Main Page

![photo_2023-07-06_00-03-57](https://github.com/muhammedaliturk/DormApp/assets/103900615/f6c3ab6f-af2a-4dad-beeb-992e13a39bed)


I'm using firebase firestore, realtime and storage database on the home page.The user's name is at the top of the screen.
Announcements/events in slider form and clickable.
when you click any Announcements/events, details of announcements/events appear in popup format

![1688243065942](https://github.com/muhammedaliturk/DormApp/assets/103900615/71925c6b-3371-4c4f-b26f-2f52570dfa50)
