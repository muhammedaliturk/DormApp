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

I am designing the DormApp to participate in Teknofest

# Login screen

![photo_2023-07-06_00-04-10](https://github.com/muhammedaliturk/DormApp/assets/103900615/42e4fdd4-3339-4218-9698-389252f13bb1)



i am using firebase for authentication on login screen. Application asks for institution id, email and password on first login.
In the next logins, the institution id and email will come automatically, just enter the password.
If you want, you can use fingerprint. 

![Screenshot_20230730_152215](https://github.com/muhammedaliturk/DormApp/assets/103900615/f93f63bd-b39b-47c6-94c3-fe794089b1d2)

After the user becomes a member, he/she becomes an unapproved user. The user cannot login to the application until the admin approves the user.


# Main Page(user interface)

![photo_2023-07-06_00-03-57](https://github.com/muhammedaliturk/DormApp/assets/103900615/f6c3ab6f-af2a-4dad-beeb-992e13a39bed)

# Main Page(admin interface)

![Screenshot_20230729_224309](https://github.com/muhammedaliturk/DormApp/assets/103900615/5649d293-82b2-48a3-9b67-666f181c2215)


I'm using firebase firestore, realtime and storage database on the home page.The user's name is at the top of the screen.
Announcements/events in slider form and clickable.
when you click any Announcements/events, details of announcements/events appear in popup format
admin can add announcements from the + button.
Users newly registered to the application are in unapproved user status. 
Admin can see unapproved users from the notification button in the upper right corner of the screen.

# Unapproved Users Screen

![Screenshot_20230730_151223](https://github.com/muhammedaliturk/DormApp/assets/103900615/33b0c7a6-6b41-49e1-88a7-957fd2229d2d)

The admin can approve the user from this page. After approval, the user can access the application.


