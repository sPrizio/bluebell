import Image from "next/image";
import styles from "./layout.module.scss";
import SimpleHero from "@/app/components/Hero/SimpleHero";

export default function Home() {

  const baseClass = "home-page"


  return (
    <div className={styles[baseClass]}>
      <SimpleHero
        title={'NOT YOUR FATHER’S FINANCIAL ADVISOR'}
        text={'We get it — you want to change careers, have kids, retire one day; but you also want to go to Portugal next year. Find out how much more you can do when you have your [financial] sh*t together.'}
        variant={'tertiary'}
        alignment={'center'}
        size={"medium"}
      />
    </div>
  );
}
