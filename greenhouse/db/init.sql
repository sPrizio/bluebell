DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'bluebell') THEN
      CREATE DATABASE bluebell;
END IF;
END
$$;