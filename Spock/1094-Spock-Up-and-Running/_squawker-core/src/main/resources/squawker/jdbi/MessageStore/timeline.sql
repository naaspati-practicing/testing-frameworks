SELECT * FROM messages WHERE 
  posted_by=(select id as user_id FROM users where username=:username) OR 
  posted_by IN(select followee_id from followings where follwer_id=user_id)  
);
