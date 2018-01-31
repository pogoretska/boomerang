package org.telegram.updateshandlers;

import java.util.ArrayList;
import java.util.List;

import org.telegram.BotConfig;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class BoomerangHandler extends TelegramLongPollingBot {

	        @Override
	        public String getBotUsername() {
	                
	                return BotConfig.BOOMERANG_USER;
	        }

	        @Override
	        public void onUpdateReceived(Update update) {

                //check if the update has a message
                if(update.hasMessage()){
                        Message message = update.getMessage();

                        //check if the message has text. it could also  contain for example a location ( message.hasLocation() )
                        if (message.hasText()) {

                                //create a object that contains the information to send back the message
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("you said: " + message.getText());
                                try {
                                        sendMessage(sendMessageRequest); //at the end, so some magic and send the message ;)
                                } catch (TelegramApiException e) {
                                        //do some error handling
                                }//end catch()
                        } else if (message.hasPhoto()) {
                        	// get the photo and send it back
                        	List<PhotoSize> userPhoto;
                        	SendPhoto sendPhotoRequest = new SendPhoto();
                        	SendMessage sendMessageRequest = new SendMessage();
                        	
                        	sendPhotoRequest.setChatId(message.getChatId().toString()); // who should get the photo
                        	sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            
                        	userPhoto = message.getPhoto();
                        	
                        	List<String> usedIds = new ArrayList<String>();
                        	
                        	for (PhotoSize photo : userPhoto) {
                        		
                        		String fileId = photo.getFileId();
                        		String uniqueId = fileId.substring(0, 26);
                        		
                        		if (!usedIds.contains(uniqueId)) {
                        			usedIds.add(uniqueId);
                        			sendPhotoRequest.setPhoto(fileId);
                        		                        		
                        			sendMessageRequest.setText("your photo goes back to you!");
                        			
                        			try {
                        			
                        				sendMessage(sendMessageRequest);
                        				sendPhoto(sendPhotoRequest); //at the end, so some magic and send the message ;)
                                    
                        			} catch (TelegramApiException e) {
                        				//do some error handling
                        			}
                        		} // end if 
                        	} // end for()
                        }

                }//end  if()

        }//end onUpdateReceived()

	        @Override
	        public String getBotToken() {
	                
	                return BotConfig.BOOMERANG_TOKEN;
	        }
}
