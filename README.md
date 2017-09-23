# Coding Test

**Assumptions**


- Art.Price is in pence so assuming we don't want decimal value. Using BigInteger as Art can be 
expensive!.

- Art.CreationDate not mentioned as mandatory but since gallery method is expecting paintings to be 
sorted based on date , I am providing a default creation date of now if not given

- Using Set to store Arts, making an assumption each piece of art should be unique in gallery.

- In statement *returns all art with creation date in the past year* assumption made that Past year means the 365 days preceding today.
Not sure if leap year should make a difference.

- In getArtists method, I am making assumption of returning one name even though you can have two or more artist with same name.
For example Raffaele from Turin and Raffele from Rome. With information in Art object its not possible to differentiate between 
two.

- If this code was going to production I would **include logging**, but for the purpose of this 
coding test I have not included, as its not mentioned in the requirements.
 
