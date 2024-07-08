import styles from "./layout.module.scss";
import SimpleHero from "@/app/components/Hero/SimpleHero";
import ColumnHero from "@/app/components/Hero/ColumnHero/ColumnHero";
import StepSection from "@/app/components/Section/Step/StepSection";
import ContactSection from "@/app/components/Section/Contact/ContactSection";
import ClientCardSection from "@/app/components/Section/ClientCard/ClientCardSection";
import boe from '@/app/assets/images/content/clientcard/BusinessOwnersExecutives.jpg';
import pr from '@/app/assets/images/content/clientcard/PreRetirees.jpg';
import pc from '@/app/assets/images/content/clientcard/PrivateClient.jpg';
import r from '@/app/assets/images/content/clientcard/Retirees.jpg';
import yp from '@/app/assets/images/content/clientcard/YoungProfessionals.jpg';

export default function Home() {

  const baseClass = "home-page"


  return (
    <div className={styles[baseClass]}>
      <SimpleHero
        title={'NOT YOUR FATHER’S FINANCIAL ADVISOR'}
        text={'We get it — you want to change careers, have kids, retire one day; but you also want to go to Portugal next year. Find out how much more you can do when you have your [financial] sh*t together.'}
        variant={'tertiary'}
        alignment={'center'}
        size={"large"}
      />

      <ClientCardSection
        title={'Your Goals, Our Mission'}
        subtitle={'At Carson Wealth, our goal is to help you pursue your goals, whether you’re just getting started, preparing for retirement, or well into your golden years.'}
        elements={[
          {
            title: 'Business owners & executives',
            text: 'I want to help my business continue to thrive, while also planning for retirement and succession.',
            image: {
              src: boe,
              alt: 'Business Executives'
            }
          },
          {
            title: 'Pre-retirees',
            text: 'I want to help my business continue to thrive, while also planning for retirement and succession.',
            image: {
              src: pr,
              alt: 'Pre-Retirees'
            }
          },
          {
            title: 'Retirees',
            text: 'I want to help my business continue to thrive, while also planning for retirement and succession.',
            image: {
              src: r,
              alt: 'Retirees'
            }
          },
          {
            title: 'Private Clients',
            text: 'I want to help my business continue to thrive, while also planning for retirement and succession.',
            image: {
              src: pc,
              alt: 'Private Clients'
            }
          },
          {
            title: 'Young Professionals',
            text: 'I want to help my business continue to thrive, while also planning for retirement and succession.',
            image: {
              src: yp,
              alt: 'Young Professionals'
            }
          }
        ]}
      />

      <StepSection
        title={'A financial advisor for young professionals'}
        elements={[
          {
            title: 'Fee-only planning',
            text: 'A one-time flat fee for a holistic financial plan (the Stash Plan). The fee covers the entirety of the Stash Plan experience. $1000 for individuals and $1500 for couples who want to go through the experience together. ',
            image: {
              src: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/4a42af0f-a9b8-4184-8e02-2df97899dcbe/Fee-only-planning.png',
              alt: 'default_image_1',
              width: 275,
              height: 144
            }
          },
          {
            title: 'Goals-based investing',
            text: 'Your goals dictate your saving and investing strategy. Stash Wealth believes investing for the sake of investing is nothing more than gambling. Our goal is to ensure you accomplish yours.',
            image: {
              src: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/1ca79d9b-31e4-4561-9da1-f904a995d16c/goals-based-investing.png',
              alt: 'default_image_2',
              width: 275,
              height: 144
            }
          },
          {
            title: 'Collaborative experience',
            text: 'Work one-on-one with a dedicated financial advisor so we can create a plan to help you accomplish what matters most to you. We lay out the specifics of living your life to the fullest both today and in the future. ',
            image: {
              src: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/ee65038f-07de-4c14-9538-3fad2b7e1229/collaborative-experience.png',
              alt: 'default_image_3',
              width: 275,
              height: 144
            }
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
      <ContactSection/>
    </div>
  );
}
