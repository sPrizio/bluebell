import styles from "./layout.module.scss";
import SimpleHero from "@/app/components/Hero/SimpleHero";
import ColumnHero from "@/app/components/Hero/ColumnHero/ColumnHero";
import StepSection from "@/app/components/Section/StepSection";

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
      <StepSection
        title={'A financial advisor for young professionals'}
        elements={[
          {
            title: 'Fee-only planning',
            text: 'A one-time flat fee for a holistic financial plan (the Stash Plan). The fee covers the entirety of the Stash Plan experience. $1000 for individuals and $1500 for couples who want to go through the experience together. ',
            image: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/4a42af0f-a9b8-4184-8e02-2df97899dcbe/Fee-only-planning.png'
          },
          {
            title: 'Goals-based investing',
            text: 'Your goals dictate your saving and investing strategy. Stash Wealth believes investing for the sake of investing is nothing more than gambling. Our goal is to ensure you accomplish yours.',
            image: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/1ca79d9b-31e4-4561-9da1-f904a995d16c/goals-based-investing.png'
          },
          {
            title: 'Collaborative experience',
            text: 'Work one-on-one with a dedicated financial advisor so we can create a plan to help you accomplish what matters most to you. We lay out the specifics of living your life to the fullest both today and in the future. ',
            image: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/ee65038f-07de-4c14-9538-3fad2b7e1229/collaborative-experience.png'
          }
        ]}
      />
      <ColumnHero
        title={'Our Firm at a Glance'}
        elements={[
          {
            title: '$25.86B',
            text: 'Assets under management'
          },
          {
            title: '2024',
            text: 'year founded'
          },
          {
            title: '12+',
            text: 'professionals'
          }
        ]}
        disclaimer={'*Reflects combined AUM as of 3.31.2024 for bluebell and its subsidiaries.'}
        variant={"secondary"}
        size={"small"}
      />
    </div>
  );
}
