# aLFaMessanger. Краткое описание
alFaMessanger - это удобный компромисс между мессенджером и социальной сетью. В этом приложении имеются: полноценный чат (с возможностью прикрепления файлов, отправки голосоывых сообщений), профиль с возможностью настроек и редактированием личных данных, возможность добавления пользователей в друзья, возможность сохранять картинки в "Сохраненное", возможность добавлять пользователей в черный список, возможность создавать свой собственный канал для публикации новостей, возможность подписываться на любой канал, возможность просматривать свои созданные каналы в отдельном экране, возможность делать свой аккаунт закрытым и скрывать статус (который показывает в сети пользователь или нет), возможность менять темы приложения.
Также в приложении есть: новостная лента, которая аккамулирует в себе все новости каналов, на которые подписан пользователь; разделение каналов и чатов в одном экране на две разные группы так, чтобы между ними можно было переключаться с помощью одного клика (в телеграме, к примеру, все перемешано); возможность производить операции над сообщенями и новостями (удаление, редактирование текста, копирование, пересылание в чаты и т.д.); функционал уведомлений внутри приложения (когда пользователя добавляют в друзья, удаляют из друзей, приглашают в скрытые каналы и т.д. с возможностью ответа на заявки); функционал Push-уведомлений; поиск по пользователям и каналам; возможность просматривать профиль другого пользователя и совершать операции с ним (звонки и т.д); возможность производить операции с картинками (открывать их на весь экран, приближать и отдалять, сохранять в Галерею, сохранять и удалять из "Сохраненного", копировать ссылку); функционал рисования граффити и операции над ним (отправлять в чат граффити, как картинку, сохранять в Галерею).
В приложении к тому же имеется проверка на соединение с интернетом и локальная база данных, которая кэширует данные во время работы приложения.
Также осуществлено раздеение возможных каналов на 3 группы: открытые, закрытые и скрытые. Открытый канал будет виден всем пользователям и каждый может вступить в него и читать нововсти канала. Закрытый канал будет виден всем пользователям, однако контент можно будет просматривать только после одобрения администратором заявки на вступление. Скрытый канал не будет виден никому. Присоединитсья к нему и просматривать его контент можно будет, когда администратор отправит пользователю приглашение.
# aLFaMessanger. Стек технологий
Используемый стек технологий: Kotlin, Navigation Component, Datastore Preferences, MVVM + LiveData, Clean architecture, Firebase (Firebase Auth, Firebase Storage, Realtime Database, Cloud Messaging), Room, Retrofit, Dagger Hilt, Picasso, Flow, Kotlin Coroutines, JUnit5, Mockito
# aLFaMessanger. Скрины приложения
![Screenshot_1](https://user-images.githubusercontent.com/94142972/193031038-5d9d7bcc-b96d-4558-b2f9-ff1e96f7d69b.png)
![Screenshot_2](https://user-images.githubusercontent.com/94142972/193031108-ac27ade8-afcc-4042-ad04-50e5ccafa1a3.png)
![Screenshot_21](https://user-images.githubusercontent.com/94142972/193031181-d54c9c36-f3a1-4bc8-9e4b-934e9831ed4d.png)
![Screenshot_22](https://user-images.githubusercontent.com/94142972/193031189-a4bfd348-7259-4ccf-95f0-fed9fa72f8f1.png)
![Screenshot_23](https://user-images.githubusercontent.com/94142972/193031191-68a0534c-6217-4c19-aea5-cbe5e831a55b.png)
![Screenshot_24](https://user-images.githubusercontent.com/94142972/193031192-f3b0bfdd-6900-445c-bb5b-1fff80c7952d.png)
![Screenshot_25](https://user-images.githubusercontent.com/94142972/193031193-9156b44b-2eba-4874-842f-2a265d33186c.png)
![Screenshot_26](https://user-images.githubusercontent.com/94142972/193031196-317e2e73-fce0-4dfd-b66f-91234847a0a8.png)
![Screenshot_3](https://user-images.githubusercontent.com/94142972/193031198-d179e23e-effd-45d2-943c-9416a9221d0a.png)
![Screenshot_4](https://user-images.githubusercontent.com/94142972/193031202-e82f5ca8-1e5f-4a80-979f-a9d5e57529be.png)
![Screenshot_5](https://user-images.githubusercontent.com/94142972/193031205-7acfb1cd-cf85-4462-baeb-bcd44eb7b0af.png)
![Screenshot_6](https://user-images.githubusercontent.com/94142972/193031208-9eb4d08f-76a7-44e8-bdc7-a2236c8b8b96.png)
![Screenshot_7](https://user-images.githubusercontent.com/94142972/193031210-51294caf-4d05-4f1d-a85a-addf575cd352.png)
![Screenshot_8](https://user-images.githubusercontent.com/94142972/193031214-d90c81b8-0987-412a-8e87-645970963fea.png)
![Screenshot_9](https://user-images.githubusercontent.com/94142972/193031218-5cb713f1-5ca7-43b9-94e4-39a25f0556a4.png)
![Screenshot_10](https://user-images.githubusercontent.com/94142972/193031221-6326a7e5-04b5-4a07-aa2a-47ffb0c4890a.png)
![Screenshot_11](https://user-images.githubusercontent.com/94142972/193031225-0cdc04ec-3770-4354-ada9-c749e407bad5.png)
![Screenshot_12](https://user-images.githubusercontent.com/94142972/193031230-35335586-521c-4290-a1c9-0073489aa40c.png)
![Screenshot_13](https://user-images.githubusercontent.com/94142972/193031235-85520867-44e1-40bd-ac47-22b3b9cdbb8d.png)
![Screenshot_27](https://user-images.githubusercontent.com/94142972/193031492-e03aa55b-3758-493c-9810-b95591bbcdbe.png)
![Screenshot_14](https://user-images.githubusercontent.com/94142972/193031236-168f4481-44ab-4225-88cd-3cb6bb890f68.png)
![Screenshot_15](https://user-images.githubusercontent.com/94142972/193031241-67d83e75-3fcd-47d0-ac0d-d3e770319b83.png)
![Screenshot_16](https://user-images.githubusercontent.com/94142972/193031243-08b680ff-084f-4211-b772-4045abd9a4ea.png)
![Screenshot_17](https://user-images.githubusercontent.com/94142972/193031246-f7a93ec6-ed25-4c85-b44c-4573cfd4497c.png)
![Screenshot_18](https://user-images.githubusercontent.com/94142972/193031249-8f3981e1-1d34-48b7-83bb-5807568fe26f.png)
![Screenshot_19](https://user-images.githubusercontent.com/94142972/193031251-ea6a268a-7332-4f20-b74d-b80f6640d340.png)
![![Screenshot_31](https://user-images.githubusercontent.com/94142972/193045701-228c42d6-5d7d-4966-bbcc-9da023fb664c.png)
![Screenshot_30](https://user-images.githubusercontent.com/94142972/193045705-a0907134-c4d3-45d1-90b9-cabffeddbe6a.png)
![Screenshot_29](https://user-images.githubusercontent.com/94142972/193045708-603dcc0f-0728-4db3-9ef6-6c75ae133463.png)
![Screenshot_28](https://user-images.githubusercontent.com/94142972/193045710-cdcaadf2-0228-47fa-979d-85d643f9bc3b.png)
![Screenshot_33](https://user-images.githubusercontent.com/94142972/193045712-4ebb2da9-e2fa-4e7c-91c0-ee6e82b1df1a.png)
![Screenshot_32](https://user-images.githubusercontent.com/94142972/193045713-cbd8116b-ed65-4ba6-a863-55471c92ab68.png)
Screenshot_20](https://user-images.githubusercontent.com/94142972/193031253-8af375b8-4a4c-4f1d-9f93-365b6e8b6abc.png)
