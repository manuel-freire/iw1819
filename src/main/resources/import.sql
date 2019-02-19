-- 
-- SQL script that will be used to initialize the database
-- Note that the syntax is very picky. A reference is available at
--   http://www.hsqldb.org/doc/guide/sqlgeneral-chapt.html
-- 
-- When writing INSERT statements, the order must be exactly as found in
-- the server logs (search for "create table user"), or as 
-- specified within (creation-compatible) parenthesis:
--     INSERT INTO user(id,enabled,login,password,roles) values (...)
-- vs
--     INSERT INTO user VALUES (...)
-- You can find the expected order by inspecting the output of Hibernate
-- in your server logs (assuming you use Spring + JPA)
--

-- On passwords:
--
-- valid bcrypt-iterated, salted-and-hashed passwords can be generated via
-- https://www.dailycred.com/article/bcrypt-calculator
-- (or any other implementation) and prepending the text '{bcrypt}'
--
-- Note that every time that you generate a bcrypt password with a given 
-- password you will get a different result, because the first characters
-- after the third $ correspond to a random salt that will be different each time.
--
-- a few simple examples:
-- {bcrypt}$2a$04$2ao4NQnJbq3Z6UeGGv24a.wRRX0FGq2l5gcy2Pjd/83ps7YaBXk9C == 'a'
-- {bcrypt}$2a$04$5v02dQ.kxt7B5tJIA4gh3u/JFQlxmoCadSnk76PnvoN35Oz.ge3GK == 'p'
-- {bcrypt}$2a$04$9rrSETFYL/gqiBxBCy3DMOIZ6qmLigzjqnOGbsNji/bt65q.YBfjK == 'q'

-- an admin with password 'a'
INSERT INTO user(id,enabled,login,password,roles) VALUES (
	1, 1, 'a', 
	'{bcrypt}$2a$04$2ao4NQnJbq3Z6UeGGv24a.wRRX0FGq2l5gcy2Pjd/83ps7YaBXk9C',
	'USER,ADMIN'
);
-- a teacher with password 'p'
INSERT INTO user(id,enabled,login,password,roles) VALUES (2, 1, 'p', 
	'{bcrypt}$2a$04$5v02dQ.kxt7B5tJIA4gh3u/JFQlxmoCadSnk76PnvoN35Oz.ge3GK', 	'USER');
-- a teacher with password 'q'
INSERT INTO user(id,enabled,login,password,roles) VALUES (3, 1, 'q', 
	'{bcrypt}$2a$04$9rrSETFYL/gqiBxBCy3DMOIZ6qmLigzjqnOGbsNji/bt65q.YBfjK', 	'USER');

